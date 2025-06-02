package com.webapp.storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ArrayStorageTest.class,
                ListStorageTest.class,
                MapStorageTest.class,
                MapResumeStorageTest.class,
                SortedArrayStorageTest.class,
                AbstractArrayStorageTest.class,
                FileStorageTest.class,
                PathStorageTest.class,
                DataStreamSerializerTest.class,
                SqlStorageTest.class
        })

public class AllStorageTest {
}