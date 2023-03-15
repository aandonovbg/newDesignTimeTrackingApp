package com.example.newDesignApp.enums;

public enum Role {
    EMPLOYEE("Служител"),
    ADMIN("Админ");

    public final String label;
    Role(String label) {
        this.label = label;
    }
}
