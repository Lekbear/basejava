package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;

import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<SK> implements Storage {
    private final Comparator<Resume> COMP = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public void clear() {
        clearAll();
    }

    public final void save(Resume resume) {
        LOG.info("Save " + resume);
        SK searchKey = getSearchKey(resume.getUuid());

        if (isExisting(searchKey)) {
            LOG.warning("Resume " + resume.getUuid() + " already exist");
            throw new ExistStorageException(resume.getUuid());
        }

        saveResume(resume, searchKey);
    }

    public final void update(Resume resume) {
        LOG.info("Update " + resume);
        setResume(resume, getNotExistingSearchKey(resume.getUuid()));
    }

    public final Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return getResume(getNotExistingSearchKey(uuid));
    }

    public final void delete(String uuid) {
        LOG.info("Delete " + uuid);
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

    private SK getNotExistingSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);

        if (!isExisting(searchKey)) {
            LOG.warning("Resume " + uuid + " not exist");
            throw new NotExistStorageException(uuid);
        }

        return searchKey;
    }

    protected abstract boolean isExisting(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    protected abstract void clearAll();

    protected abstract void saveResume(Resume resume, SK searchKey);

    protected abstract void setResume(Resume resume, SK searchKey);

    protected abstract Resume getResume(SK searchKey);

    protected abstract void deleteResume(SK searchKey);

    protected abstract List<Resume> getList();

    protected abstract int getSize();
}
