package com.basejava.webapp.model;

public enum ContactType {
    TELEPHONE("телефон"),
    SKYPE("skype"),
    EMAIL("e-mail");

    private final String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
