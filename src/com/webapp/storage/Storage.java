package com.webapp.storage;

import com.webapp.model.Resume;

import java.util.List;


public interface Storage {

    void save(Resume resume);

    Resume get(String uuid);

    void update(Resume resume);

    void delete(String uuid);

    void clear();

    //List<Resume> getAll();

    int size();

    List<Resume> getAllSorted();
}
