package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends Section {

    @Serial
    private static final long serialVersionUID = 1L;
    private List<String> list;
    private List<String> items;
    public static final ListSection EMPTY = new ListSection("");

    public ListSection(String... list) {
        this(Arrays.asList(list));
    }

    public ListSection(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public String getTextRepresentation() {
        return String.join(", ", list);
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


    public List<String> getItems() {
        return items;
    }
}
