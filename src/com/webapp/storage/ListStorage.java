package com.webapp.storage;

import com.webapp.model.Resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListStorage extends AbstractStorage<Integer>{

    private final List<Resume> list = new ArrayList<>();

    @Override
    protected void doSave(Resume resume, Integer searchKey) {
        list.add(resume);
    }

    @Override
    protected void doUpdate(Resume resume, Integer searchKey) {
        list.set(searchKey, resume);
    }

    @Override
    protected void doDelete(Integer searchKey) {
        list.remove(searchKey.intValue());
    }

    @Override
    protected Resume doGet(Integer searchKey) {
        return list.get(searchKey);
    }

    @Override
    public boolean isExist(Integer searchKey) {
        if (searchKey != null) {
            return searchKey >= 0 && searchKey < list.size();
        }
        return false;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return List.of();
    }

    @Override
    public void clear() {
        list.clear();
    }

    public List<Resume> getAll() {
        List<Resume> sortedList = new ArrayList<>(list);
        Collections.sort(sortedList);
        return sortedList;
    }

    public List<Resume> getAllSorted() {
        return list.stream()
                .sorted(Comparator.comparing(Resume::getFullName)
                        .thenComparing(Resume::getUuid))
                .toList();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public Integer getSearchKey(String uuid) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return null;
    }
}
