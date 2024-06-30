package com.basejava.webapp.model;

import java.util.*;

public class Resume implements Comparable<Resume>{

    // Unique identifier
    private final String uuid;
    private final String fullName;

    public Resume() {
        this(UUID.randomUUID().toString(), Resume.getRandomFullName());
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public int compareTo(Resume resume) {
        return uuid.compareTo(resume.uuid);
    }

    public static String getRandomFullName() {
        List<String> firstNames = new ArrayList<>(Arrays.asList("first_name_1", "first_name_2", "first_name_3"));
        List<String> patronymicNames = new ArrayList<>(Arrays.asList("patronymic_1", "patronymic_2", "patronymic_3"));
        List<String> lastNames = new ArrayList<>(Arrays.asList("last_name_1", "last_name_2", "last_name_3"));
        Random random = new Random();
        String randomFirstName = firstNames.get(random.nextInt(firstNames.size()));
        String randomPatronymicName = firstNames.get(random.nextInt(patronymicNames.size()));
        String randomLastName = firstNames.get(random.nextInt(lastNames.size()));
        return randomFirstName + " " + randomPatronymicName + " " + randomLastName;
    }
}
