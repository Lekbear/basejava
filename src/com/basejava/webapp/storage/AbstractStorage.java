package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    private final Comparator<Resume> COMP = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public void clear() {
        clearAll();
    }

    public final void save(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());

        if (isExisting(searchKey)) {
            throw new ExistStorageException(resume.getUuid());
        }

        saveResume(resume, searchKey);
    }

    public final void update(Resume resume) {
        setResume(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    public final Resume get(String uuid) {
        return getResume(getNotExistingSearchKey(uuid));
    }

    public final void delete(String uuid) {
        deleteResume(getNotExistingSearchKey(uuid));
    }

    public List<Resume> getAllSorted() {
        List<Resume> list = getList();
        list.sort(COMP);
        return list;
    }

    public int size() {
        return getSize();
    }

    private Object getNotExistingSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);

        if (!isExisting(searchKey)) {
            throw new NotExistStorageException(uuid);
        }

        return searchKey;
    }

    protected abstract boolean isExisting(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract void clearAll();

    protected abstract void saveResume(Resume resume, Object searchKey);

    protected abstract void setResume(Resume resume, Object searchKey);

    protected abstract Resume getResume(Object searchKey);

    protected abstract void deleteResume(Object searchKey);

    protected abstract List<Resume> getList();

    protected abstract int getSize();
}
