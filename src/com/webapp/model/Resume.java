package com.webapp.model;

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
    private String uuid;
    private String fullName;
    public static final Resume EMPTY = new Resume();

    private final Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private final Map<TypeSection, Section> sections = new EnumMap<>(TypeSection.class);

    public Resume() {}
    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.uuid = uuid;
        this.fullName = fullName;
    }

    public String getContact(ContactType contact) {
        return contacts.get(contact);
    }

    public Section getSection(TypeSection section) {
        return sections.get(section);
    }

    public void setContact(ContactType contactType, String contactValue) {
        contacts.put(contactType, contactValue);
    }

    public void setSection(TypeSection typeSection, Section section) {
        sections.put(typeSection, section);
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<TypeSection, Section> getSections(TypeSection achievement) {
        return sections;
    }

    public Map<TypeSection, Section> getSections() {
        return sections;
    }

    @Override
    public int compareTo(Resume other) {
        return this.fullName.compareTo(other.fullName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resume resume = (Resume) o;
        return Objects.equals(uuid, resume.uuid) &&
                Objects.equals(fullName, resume.fullName) &&
                Objects.equals(contacts, resume.contacts) &&
                Objects.equals(sections, resume.sections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, sections);
    }

    @Override
    public String toString() {
        return "Resume{" +
                "uuid='" + uuid + '\'' +
                ", fullName='" + fullName + '\'' +
                ", contacts=" + contacts +
                ", sections=" + sections +
                '}';
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

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(String updatedName) {
        this.fullName = updatedName;
    }

    public String getName() {
        return fullName;
    }


    public void addContact(ContactType type, String value) {
        contacts.put(type, value);
    }

    public void addSection(TypeSection type, Section section) {
        sections.put(type, section);
    }
}