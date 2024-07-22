package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serialization.ObjectStreamStorage;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamStorage()));
    }
}
