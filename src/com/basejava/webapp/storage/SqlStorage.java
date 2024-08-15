package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            insertContacts(conn, r);
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
                    DELETE FROM contact
                     WHERE contact.resume_uuid = ?""", ps -> {
                ps.setString(1, r.getUuid());
                ps.execute();
            });

            insertContacts(conn, r);
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

            do {
                putContact(rs, resume);
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
                              ON resume.uuid = contact.resume_uuid
                 ORDER BY resume.full_name, resume.uuid""", (ps) -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("uuid");

                if (resumes.isEmpty() || !uuid.equals(resumes.getLast().getUuid())) {
                    resumes.add(new Resume(uuid, rs.getString("full_name")));
                }

                putContact(rs, resumes.getLast());
            }

            return resumes;
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

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        sqlHelper.executeByConnection(conn, """
                INSERT INTO contact (resume_uuid, type, value)
                VALUES (?, ?, ?)""", ps -> {
            for (Map.Entry<ContactType, String> entry : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        });
    }

    private void putContact(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("type");

        if (type != null) {
            resume.putContact(ContactType.valueOf(type), rs.getString("value"));
        }
    }
}
