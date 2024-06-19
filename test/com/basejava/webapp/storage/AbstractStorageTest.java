package com.basejava.webapp.storage;

import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String NOT_EXIST_UUID = "dummy";

    protected static final Resume RESUME_1 = new Resume(UUID_1);
    protected static final Resume RESUME_2 = new Resume(UUID_2);
    protected static final Resume RESUME_3 = new Resume(UUID_3);
    protected static final Resume RESUME_4 = new Resume(UUID_4);
    protected static final Resume RESUME_NOT_EXIST = new Resume(NOT_EXIST_UUID);

    protected static final Resume[] EMPTY_RESUMES = new Resume[]{};
    protected static final Resume[] DEFAULT_RESUMES = {RESUME_1, RESUME_2, RESUME_3};

    public AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        for (Resume resume : DEFAULT_RESUMES) {
            storage.save(resume);
        }
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertGetAll(EMPTY_RESUMES);
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_1);
    }

    @Test
    public void update() {
        storage.update(RESUME_1);
        assertGet(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_NOT_EXIST);
    }

    @Test
    public void get() {
        assertGet(RESUME_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(NOT_EXIST_UUID);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(RESUME_2.getUuid());
        assertSize(2);
        assertGet(RESUME_2);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(NOT_EXIST_UUID);
    }

    @Test
    public void getAll() {
        assertGetAll(DEFAULT_RESUMES);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    protected void assertSize(int size) {
        Assert.assertEquals(size, storage.size());
    }

    protected void assertGet(Resume resume) {
        Assert.assertSame(resume, storage.get(resume.getUuid()));
    }

    protected void assertGetAll(Resume[] resumes) {
        Resume[] array = storage.getAll();
        Arrays.sort(array);
        assertArrayEquals(resumes, array);
    }
}
