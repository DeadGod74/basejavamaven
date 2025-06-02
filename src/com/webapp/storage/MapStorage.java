package com.webapp.storage;

import com.webapp.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage<String> {
    private final Map<String, Resume> map = new LinkedHashMap<>();

    @Override
    public String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume resume, String searchKey) {
        map.put(searchKey, resume);
    }

    @Override
    public boolean isExist(String searchKey) {
        return map.containsKey(searchKey);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return List.of();
    }

    @Override
    protected void doSave(Resume resume, String searchKey) {
        map.put(searchKey, resume);
    }

    @Override
    protected Resume doGet(String searchKey) {
        return map.get(searchKey);
    }

    @Override
    protected void doDelete(String searchKey) {
        map.remove(searchKey);
    }

    @Override
    public void clear() {
        map.clear();
    }

    public List<Resume> getAllSorted() {
        return map.values().stream()
                .sorted(Comparator.comparing(Resume::getFullName)
                        .thenComparing(Resume::getUuid))
                .toList();
    }

    @Override
    public int size() {
        return map.size();
    }

}