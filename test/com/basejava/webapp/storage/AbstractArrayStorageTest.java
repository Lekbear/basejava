package com.basejava.webapp.storage;

import com.basejava.webapp.ResumeTestData;
import com.basejava.webapp.exception.StorageException;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected static final String UUID_ = "uuid_";
    protected static final String FULL_NAME_ = "full_name_";

    public AbstractArrayStorageTest(Storage storage) {
        super(storage);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        String uuid;
        String fullName;

        try {
            for (int i = storage.size(); i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                uuid = UUID_ + (i + 1);
                fullName = FULL_NAME_ + (i + 1);
                storage.save(ResumeTestData.getNewResume(uuid, fullName));
            }
        } catch (StorageException e) {
            Assert.fail("Overflow happened earlier");
        }

        uuid = UUID_ + (storage.size() + 1);
        fullName = FULL_NAME_ + (storage.size() + 1);
        storage.save(ResumeTestData.getNewResume(uuid, fullName));
    }
}