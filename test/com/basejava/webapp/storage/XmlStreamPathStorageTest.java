package com.basejava.webapp.storage;

import com.basejava.webapp.storage.serializer.XmlStreamSerializer;

import java.nio.file.Paths;

public class XmlStreamPathStorageTest extends AbstractStorageTest {

    public XmlStreamPathStorageTest() {
        super(new PathStorage(Paths.get(STORAGE_DIR.getAbsolutePath()), new XmlStreamSerializer()));
    }
}
