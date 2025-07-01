package com.webapp.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.io.Serial;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class TextSection extends Section {
    @Serial
    private static final long serialVersionUID = 1L;

    private String text;

    public static final TextSection EMPTY = new TextSection("");

    public TextSection(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextSection)) return false;
        TextSection section = (TextSection) o;
        return Objects.equals(text, section.text);
    }

    @Override
    public int hashCode() {
        return text != null ? text.hashCode() : 0;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextRepresentation() {
        StringBuilder representation = new StringBuilder();

        if (text != null && !text.isEmpty()) {
            String[] lines = text.split("\n");
            for (String line : lines) {
                representation.append(line.trim()).append("\n");
            }
        }

        return representation.toString().trim();
    }
}
