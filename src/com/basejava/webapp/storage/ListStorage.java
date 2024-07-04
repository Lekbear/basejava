package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer> {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected boolean isExisting(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    protected Integer getSearchKey(String uuid) {
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
    protected void saveResume(Resume resume, Integer searchKey) {
        storage.add(resume);
    }

    @Override
    protected void setResume(Resume resume, Integer searchKey) {
        storage.set(searchKey, resume);
    }

    @Override
    protected Resume getResume(Integer searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected void deleteResume(Integer searchKey) {
        storage.remove(searchKey.intValue());
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
