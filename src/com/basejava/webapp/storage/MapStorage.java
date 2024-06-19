package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected boolean isExisting(Object searchKey) {
        return storage.containsKey((String) searchKey);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void clearAll() {
        storage.clear();
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void setResume(Resume resume, Object searchKey) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get((String) searchKey);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        storage.remove((String) searchKey);
    }

    @Override
    protected Resume[] getArray() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    protected int getSize() {
        return storage.size();
    }
}
