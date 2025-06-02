package com.webapp.storage;

import com.webapp.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage<Resume> {
    private final Map<Resume, Resume> map = new LinkedHashMap<>();

    @Override
    public Resume getSearchKey(String uuid) {
        for (Resume resume : map.keySet()) {
            if (resume.getUuid().equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    @Override
    protected void doUpdate(Resume resume, Resume searchKey) {
        if (searchKey instanceof Resume existingResume) {
            map.put(existingResume, resume);
        }
    }

    @Override
    public boolean isExist(Resume searchKey) {
        if (searchKey instanceof Resume resume) {
            return map.containsKey(resume);
        }
        return false;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    protected void doSave(Resume resume, Resume searchKey) {
        map.put(resume, resume);
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        if (searchKey instanceof Resume resume) {
            return map.get(resume);
        }
        return null;
    }

    @Override
    protected void doDelete(Resume searchKey) {
        if (searchKey instanceof Resume resume) {
            map.remove(resume);
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

}