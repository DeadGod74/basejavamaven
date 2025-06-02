package com.webapp.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)

public abstract class Section implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    public abstract List<Company> getContent();

    public Section() {
    }

    public abstract String getTextRepresentation();
}
