package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected boolean isExisting(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    protected Object getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }

        return -1;
    }

    @Override
    protected void clearAll() {
        storage.clear();
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        storage.add(resume);
    }

    @Override
    protected void setResume(Resume resume, Object searchKey) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected void deleteResume(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected List<Resume> getList() {
        return new ArrayList<>(storage);
    }

    @Override
    protected int getSize() {
        return storage.size();
    }
}
