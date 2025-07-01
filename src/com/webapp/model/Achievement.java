package com.webapp.model;

import java.util.List;

public class Achievement{
    private String type;
    private String content;
    private List<Company> companies;

    public Achievement(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    @Override
    public String toString() {
        return
                "type='" + type + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
