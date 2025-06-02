package com.webapp.storage;

import com.webapp.exception.StorageException;
import com.webapp.model.Resume;
import org.junit.Before;
import org.junit.Test;

public class AbstractArrayStorageTest {

    protected Storage storage;

    @Before
    public void setUp() {
        storage = new ArrayStorage();
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() throws Exception {
        for (int i = 1; i <= AbstractArrayStorage.STORAGE_LIMIT; i++) {
            storage.save(new Resume("Name" + i));
        }
        storage.save(new Resume("Overflow"));
    }
}