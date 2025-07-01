package com.webapp.util;

import com.webapp.model.ListSection;

import java.util.List;

public class ListSectionWrapper {
    private String type;
    private List<String> list;

    public ListSectionWrapper(ListSection listSection) {
        this.type = listSection.getType();
        this.list = listSection.getList();
    }

    public String getType() {
        return type;
    }

    public List<String> getList() {
        return list;
    }
}
