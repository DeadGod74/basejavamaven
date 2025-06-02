package com.webapp.storage;

import com.webapp.storage.serializer.ObjectStreamStorage;

public class PathStorageTest extends AbstractStorageTest {

    public PathStorageTest() {
        super(new PathStorage(FILE_PATH.getAbsolutePath(), new ObjectStreamStorage()));
    }
}