package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.DataStreamSerializer;

import java.nio.file.Paths;

public class DataStreamPathStorageTest extends AbstractStorageTest {

    public DataStreamPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR.getAbsolutePath()), new DataStreamSerializer()));
    }
}
