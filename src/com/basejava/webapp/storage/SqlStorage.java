package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.executeTransaction(conn -> {
            sqlHelper.executeByConnection(conn, """
                    INSERT INTO resume(uuid, full_name)
                    VALUES (?, ?)""", ps -> {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            });

            sqlHelper.executeByConnection(conn, """
                    INSERT INTO contact (resume_uuid, type, value)
                    VALUES (?, ?, ?)""", ps -> insertContacts(ps, r));
        });
    }

    @Override
    public void update(Resume r) {
        sqlHelper.executeTransaction(conn -> {
            sqlHelper.executeByConnection(conn, """
                    UPDATE resume
                       SET full_name = ?
                     WHERE uuid = ?;""", ps -> {
                ps.setString(1, r.getFullName());
                ps.setString(2, r.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(r.getUuid());
                }
            });

            sqlHelper.executeByConnection(conn, """
                    INSERT INTO contact(resume_uuid, type, value)
                    VALUES (?, ?, ?)
                        ON CONFLICT (resume_uuid, type) DO UPDATE
                       SET value = EXCLUDED.value""", ps -> insertContacts(ps, r));
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("""
                SELECT resume.uuid, resume.full_name, contact.type, contact.value
                  FROM resume
                       LEFT JOIN contact
                              ON resume.uuid = contact.resume_uuid
                 WHERE resume.uuid = ?""", (ps) -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }

            Resume resume = new Resume(uuid, rs.getString("full_name"));
            String type = rs.getString("type");
            System.out.println(type);
            do {
                resume.putContact(ContactType.valueOf(rs.getString("type")),
                        rs.getString("value"));
            } while (rs.next());

            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.execute("""
                SELECT resume.uuid, resume.full_name, contact.type, contact.value
                  FROM resume
                       LEFT JOIN contact
                              ON resume.uuid = contact.resume_uuid""", (ps) -> {
            Map<String, Resume> resumes = new HashMap<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume resume = resumes.get(uuid);

                if (resume == null) {
                    resume = new Resume(uuid, rs.getString("full_name"));
                }

                resume.putContact(ContactType.valueOf(rs.getString("type")),
                        rs.getString("value"));
                resumes.put(uuid, resume);
            }

            return resumes.values()
                    .stream()
                    .sorted(AbstractStorage.COMP)
                    .collect(Collectors.toList());
        });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(uuid) FROM resume", (ps) -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        });
    }

    private void insertContacts(PreparedStatement ps, Resume r) throws SQLException {
        for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
            ps.setString(1, r.getUuid());
            ps.setString(2, entry.getKey().name());
            ps.setString(3, entry.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
    }
}
