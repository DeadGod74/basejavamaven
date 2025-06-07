package com.webapp.model;

public enum ContactType {
    PHONE("Телефон"),
    SKYPE("Skype") {
        @Override
        public String toHtml0 (String value) {
            return "<a href='skype:" + value + "'>" + value + "</a>";
        }
    },
    MAIL("Электронная почта") {
        @Override
        public String toHtml0 (String value) {
            return "<a href='mailto:" + value + "'>" + value + "</a>";
        }
    },
    PROFILE_LINKEDN("Профиль LinkedIn"),
    PROFILE_GITHUB("Профиль GitHub"),
    PROFILE_STACKOVERFLOW("Профиль Stackoverflow"),
    HOME_PAGE("Домашняя страница");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    protected String toHtml0(String value) {
        return title + ": " + value;
    }

    public String toHtml(String value) {
        return (value == null) ?"" : toHtml0(value);
    }

}
