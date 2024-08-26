package com.basejava.webapp.storage;

import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.ContactType;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.model.Section;
import com.basejava.webapp.model.SectionType;
import com.basejava.webapp.sql.SqlHelper;
import com.basejava.webapp.util.JsonParser;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new StorageException(e);
        }
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
            insertSections(conn, r);
            return null;
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

            sqlHelper.executeByConnection(conn, """
                    DELETE FROM section
                     WHERE section.resume_uuid = ?""", ps -> {
                ps.setString(1, r.getUuid());
                ps.execute();
            });

            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("""
                SELECT resume.uuid, resume.full_name,\s
                       contact.type, contact.value,\s
                       section.type AS section_type, section.value AS section_value
                  FROM resume
                       LEFT JOIN contact
                              ON resume.uuid = contact.resume_uuid
                       LEFT JOIN section
                              ON resume.uuid = section.resume_uuid
                 WHERE resume.uuid = ?""", (ps) -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }

            Resume resume = new Resume(uuid, rs.getString("full_name"));

            do {
                putContact(rs, resume);
                putSection(rs, resume);
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
        return getAllSortedSelection(SelectType.SEPARATELY);
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

    private void insertSections(Connection conn, Resume r) throws SQLException {
        sqlHelper.executeByConnection(conn, """
                INSERT INTO section (resume_uuid, type, value)
                VALUES (?, ?, ?)""", ps -> {
            for (Map.Entry<SectionType, Section> entry : r.getSections().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, entry.getKey().name());
                ps.setString(3, JsonParser.write(entry.getValue(), Section.class));
                ps.addBatch();
            }
            ps.executeBatch();
        });
    }

    private void putSection(ResultSet rs, Resume resume) throws SQLException {
        String type = rs.getString("section_type");

        if (type != null) {
            ;
            resume.putSection(SectionType.valueOf(type),
                    JsonParser.read(rs.getString("section_value"), Section.class));
        }
    }

    private List<Resume> getAllSortedJoin() {
        return sqlHelper.execute("""
                SELECT resume.uuid, resume.full_name,
                       contact.type, contact.value,
                       section.type AS section_type, section.value AS section_value
                  FROM resume
                       LEFT JOIN contact
                              ON resume.uuid = contact.resume_uuid
                       LEFT JOIN section
                              ON resume.uuid = section.resume_uuid
                 ORDER BY resume.full_name, resume.uuid""", (ps) -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("uuid");

                if (resumes.isEmpty() || !uuid.equals(resumes.getLast().getUuid())) {
                    resumes.add(new Resume(uuid, rs.getString("full_name")));
                }

                putContact(rs, resumes.getLast());
                putSection(rs, resumes.getLast());
            }

            return resumes;
        });
    }

    private List<Resume> getAllSortedSeparately() {
        return sqlHelper.executeTransaction(conn -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            sqlHelper.executeByConnection(conn, """
                    SELECT resume.uuid, resume.full_name
                      FROM resume
                     ORDER BY resume.full_name, resume.uuid""", ps -> {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    resumes.put(uuid, new Resume(uuid, rs.getString("full_name")));
                }
            });

            sqlHelper.executeByConnection(conn, """
                    SELECT resume_uuid, type, value
                      FROM contact""", ps -> {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    putContact(rs, resumes.get(rs.getString("resume_uuid")));
                }
            });

            sqlHelper.executeByConnection(conn, """
                    SELECT resume_uuid, type AS section_type, value AS section_value
                      FROM section""", ps -> {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    putSection(rs, resumes.get(rs.getString("resume_uuid")));
                }
            });

            return resumes.values().stream().toList();
        });
    }

    private enum SelectType {
        JOIN,
        SEPARATELY,
    }

    private List<Resume> getAllSortedSelection(SelectType option) {
        return switch (option) {
            case JOIN -> getAllSortedJoin();
            case SEPARATELY -> getAllSortedSeparately();
        };
    }
}
