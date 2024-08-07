package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import com.basejava.webapp.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {
    public final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(() -> DriverManager.getConnection(dbUrl, dbUser, dbPassword));
    }

    @Override
    public void clear() {
        sqlHelper.tryConnection("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void save(Resume r) {
        sqlHelper.tryConnection("INSERT INTO resume(uuid, full_name) VALUES (?, ?)", (ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            if (ps.executeUpdate() == 0) {
                throw new ExistStorageException(r.getUuid());
            }
        }));
    }

    @Override
    public void update(Resume r) {
        sqlHelper.tryConnection("UPDATE resume SET full_name = ? WHERE uuid = ?;", (ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(r.getUuid());
            }
        }));
    }

    @Override
    public Resume get(String uuid) {
        final String[] fullName = new String[1];
        sqlHelper.tryConnection("SELECT * FROM resume WHERE uuid = ?", (ps) -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            fullName[0] = rs.getString("full_name");
        });
        return new Resume(uuid, fullName[0]);
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.tryConnection("DELETE FROM resume WHERE uuid = ?", (ps) -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> resumes = new ArrayList<>();
        sqlHelper.tryConnection("SELECT trim(uuid) as uuid, full_name FROM resume ORDER BY full_name, uuid", (ps) -> {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
            }
        });

        return resumes;
    }

    @Override
    public int size() {
        final int[] count = {0};
        sqlHelper.tryConnection("SELECT COUNT(uuid) FROM resume", (ps) -> {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count[0] = rs.getInt(1);
            }
        });
        return count[0];
    }
}
