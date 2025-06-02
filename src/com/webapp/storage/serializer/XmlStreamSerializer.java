package com.webapp.storage.serializer;

import com.webapp.model.*;
import com.webapp.util.XmlParser;


import java.io.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlStreamSerializer implements Serialization {
    private XmlParser xmlParser;

    public XmlStreamSerializer() {
        xmlParser = new XmlParser(
                Resume.class, Company.class, CompanySection.class, ListSection.class, Period.class, Section.class, TextSection.class
        );
    }

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance(Resume.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(r, os);
        } catch (Exception e) {
            throw new IOException("Error during XML serialization", e);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance(Resume.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (Resume) unmarshaller.unmarshal(is);
        } catch (Exception e) {
            throw new IOException("Error during XML deserialization", e);
        }
    }
}