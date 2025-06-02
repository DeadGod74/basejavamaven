package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends Section{
    @Serial
    private static final long serialVersionUID = 1L;
    private List<String> list;

    public ListSection() {
    }

    public ListSection(String... list) {
        this(Arrays.asList(list));
    }

    public ListSection(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    @Override
    public String toString() {
        return String.join(", ", list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListSection)) return false;
        ListSection section = (ListSection) o;
        return Objects.equals(list, section.list);
    }

    @Override
    public int hashCode() {
        return list != null ? list.hashCode() : 0;
    }

    @Override
    public List<Company> getContent() {
        return Collections.emptyList();
    }

    public String getTextRepresentation() {
        return String.join(", ", list);
    }
}
