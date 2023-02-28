package com.order.bachlinh.core.entities.model;

public enum Gender {
    MALE,
    FEMALE;

    public static Gender of(String genderName) {
        for (Gender g : Gender.values()) {
            if (g.name().equals(genderName.toUpperCase())) {
                return g;
            }
        }
        // Never cause
        return null;
    }
}
