package com.basejava.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // Unique identifier
    private final String uuid;
    private String fullName;
    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<SectionType, Section> sections = new EnumMap<>(SectionType.class);

    public Resume() {
        this(UUID.randomUUID().toString(), "");
    }

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        Objects.requireNonNull(uuid, "uuid must not be null");
        Objects.requireNonNull(fullName, "fullName must not be null");
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        StringBuilder ans = new StringBuilder("Resume {" +
                "\nuuid = '" + uuid + '\'' +
                ", \nfullName = '" + fullName + '\'' +
                ", \ncontacts = {");
        for (Map.Entry<ContactType, String> entry : contacts.entrySet()) {
            ans.append("\n\t");
            ans.append(entry.getKey());
            ans.append(" : ");
            ans.append(entry.getValue());
        }

        ans.append("\n},\nsections = {");

        for (Map.Entry<SectionType, Section> entry : sections.entrySet()) {
            ans.append("\n\t");
            ans.append(entry.getKey());
            ans.append(" : ");
            ans.append(entry.getValue());
        }

        ans.append("\n\t}\n}");

        return ans.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName) && contacts.equals(resume.contacts) &&
                sections.equals(resume.sections);
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + fullName.hashCode();
        result = 31 * result + contacts.hashCode();
        result = 31 * result + sections.hashCode();
        return result;
    }

    @Override
    public int compareTo(Resume resume) {
        return uuid.compareTo(resume.uuid);
    }

    public void putContact(ContactType contactType, String contact) {
        contacts.put(contactType, contact);
    }

    public void putAllContact(HashMap<ContactType, String> map) {
        contacts.putAll(map);
    }

    public String getContact(ContactType contactType) {
        return contacts.get(contactType);
    }

    public void putSection(SectionType sectionType, Section section) {
        sections.put(sectionType, section);
    }

    public void putAllSection(HashMap<SectionType, Section> map) {
        sections.putAll(map);
    }

    public Section getSection(SectionType sectionType) {
        return sections.get(sectionType);
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getSections() {
        return sections;
    }
}
