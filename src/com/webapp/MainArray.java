package com.webapp;

import com.webapp.model.Resume;
import com.webapp.storage.ArrayStorage;
import com.webapp.storage.Storage;

public class MainArray {
    private final static Storage storage = new ArrayStorage();

    public static void main(String[] args) {

        final Resume r1 = new Resume("uuid1");
        final Resume r2 = new Resume("uuid2");
        final Resume r3 = new Resume("uuid3");

        storage.save(r1);
        storage.save(r2);

        System.out.println(storage.get("uuid1"));

        storage.delete("uuid1");

        System.out.println(storage.size());

        storage.clear();

        System.out.println(storage.size());
    }
}
