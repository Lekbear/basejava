package com.basejava.webapp.storage;

import com.basejava.webapp.exception.StorageException;
import com.basejava.webapp.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    public static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    protected boolean isExisting(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    protected void clearAll() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    protected void saveResume(Resume resume, Object searchKey) {
        if (size >= STORAGE_LIMIT) {
            throw new StorageException("Storage overflow", resume.getUuid());
        } else {
            insert(resume, (int) searchKey);
            size++;
        }
    }

    @Override
    protected void setResume(Resume resume, Object searchKey) {
        storage[(int) searchKey] = resume;
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage[(int) searchKey];
    }

    @Override
    protected void deleteResume(Object searchKey) {
        remove((int) searchKey);
        storage[--size] = null;
    }

    @Override
    protected Resume[] getArray() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected int getSize() {
        return size;
    }

    protected abstract void insert(Resume resume, int index);

    protected abstract void remove(int index);
}
