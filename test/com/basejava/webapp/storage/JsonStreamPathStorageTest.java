package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.JsonStreamSerializer;

import java.nio.file.Paths;

public class JsonStreamPathStorageTest extends AbstractStorageTest {

    public JsonStreamPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR.getAbsolutePath()), new JsonStreamSerializer()));
    }
}
