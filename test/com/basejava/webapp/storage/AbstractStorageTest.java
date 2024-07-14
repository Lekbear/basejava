package com.basejava.webapp.storage;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.exception.ExistStorageException;
import com.basejava.webapp.exception.NotExistStorageException;
import com.basejava.webapp.model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public abstract class AbstractStorageTest {
    protected final Storage storage;

    protected static final String UUID_1 = "uuid1";
    protected static final String UUID_2 = "uuid2";
    protected static final String UUID_3 = "uuid3";
    protected static final String UUID_4 = "uuid4";
    protected static final String NOT_EXIST_UUID = "dummy";
    protected static final String NOT_EXIST_FULL_NAME = "full_name_dummy";

    protected static final String FULL_NAME_1 = "full_name_1";
    protected static final String FULL_NAME_2 = "full_name_2";
    protected static final String FULL_NAME_3 = "full_name_3";
    protected static final String FULL_NAME_4 = "full_name_4";

    protected static final Resume RESUME_1 = ResumeTestData.getNewResume(UUID_1, FULL_NAME_1);
    protected static final Resume RESUME_2 = ResumeTestData.getNewResume(UUID_2, FULL_NAME_2);
    protected static final Resume RESUME_3 = ResumeTestData.getNewResume(UUID_3, FULL_NAME_3);
    protected static final Resume RESUME_4 = ResumeTestData.getNewResume(UUID_4, FULL_NAME_4);
    protected static final Resume RESUME_NOT_EXIST = ResumeTestData.getNewResume(NOT_EXIST_UUID, NOT_EXIST_FULL_NAME);

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
        assertGetAllSorted(EMPTY_RESUMES);
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
    public void getAllSorted() {
        assertGetAllSorted(DEFAULT_RESUMES);
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

    protected void assertGetAllSorted(Resume[] resumes) {
        List<Resume> list = storage.getAllSorted();
        assertArrayEquals(resumes, list.toArray());
    }
}
