package com.webapp.storage;

import com.webapp.storage.serializer.ObjectStreamStorage;

public class FileStorageTest extends AbstractStorageTest {

    public FileStorageTest() {
        super(new FileStorage(FILE_PATH, new ObjectStreamStorage() {
        }));
    }

}