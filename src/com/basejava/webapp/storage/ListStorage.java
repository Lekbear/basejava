package com.basejava.webapp.storage;

import com.basejava.webapp.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected int getIndex(String uuid) {
        return storage.indexOf(new Resume(uuid));
    }

    @Override
    protected void clearAll() {
        storage.clear();
    }

    @Override
    protected void saveResume(Resume resume, int index) {
        storage.add(resume);
    }

    @Override
    protected void setResume(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    protected Resume getResume(int index) {
        return storage.get(index);
    }

    @Override
    protected void deleteResume(int index) {
        storage.remove(index);
    }

    @Override
    protected Resume[] getArray() {
        Resume[] array = new Resume[storage.size()];
        storage.toArray(array);
        return array;
    }

    @Override
    protected int getSize() {
        return storage.size();
    }
}
