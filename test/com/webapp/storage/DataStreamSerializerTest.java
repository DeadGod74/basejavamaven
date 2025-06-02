package com.webapp.storage;

import com.webapp.storage.serializer.DataStreamSerializer;

public class DataStreamSerializerTest extends AbstractStorageTest {

    public DataStreamSerializerTest() {
        super(new PathStorage(FILE_PATH.getAbsolutePath(), new DataStreamSerializer()));
    }
}