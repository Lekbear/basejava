package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;

public abstract class AbstractStorage implements Storage {
    public void clear() {
        clearAll();
    }

    public final void save(Resume resume) {
        int index = getIndex(resume.getUuid());

        if (index >= 0) {
            throw new ExistStorageException(resume.getUuid());
        }

        saveResume(resume, index);
    }

    public final void update(Resume resume) {
        setResume(resume, getIndexCheckNotExist(resume.getUuid()));
    }

    public final Resume get(String uuid) {
        return getResume(getIndexCheckNotExist(uuid));
    }

    public final void delete(String uuid) {
        deleteResume(getIndexCheckNotExist(uuid));
    }

    public Resume[] getAll() {
        return getArray();

    }

    public int size() {
        return getSize();
    }

    private int getIndexCheckNotExist(String uuid) {
        int index = getIndex(uuid);

        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }

        return index;
    }

    protected abstract int getIndex(String uuid);

    protected abstract void clearAll();

    protected abstract void saveResume(Resume resume, int index);

    protected abstract void setResume(Resume resume, int index);

    protected abstract Resume getResume(int index);

    protected abstract void deleteResume(int index);

    protected abstract Resume[] getArray();

    protected abstract int getSize();
}
