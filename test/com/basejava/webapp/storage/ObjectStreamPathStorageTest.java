package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serialization.ObjectStreamStorage;

import java.nio.file.Paths;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {

    public ObjectStreamPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR.getAbsolutePath()), new ObjectStreamStorage()));
    }
}
