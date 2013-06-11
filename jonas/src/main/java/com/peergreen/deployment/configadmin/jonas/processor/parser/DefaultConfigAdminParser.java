package com.peergreen.deployment.configadmin.jonas.processor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;

/**
 * User: guillaume
 * Date: 30/03/13
 * Time: 09:01
 */
@Component
@Instantiate
@Provides
public class DefaultConfigAdminParser implements ConfigAdminParser {
    public static final QName PROPERTY_QNAME              = new QName(NAMESPACE, "property");
    public static final QName FACTORY_CONFIGURATION_QNAME = new QName(NAMESPACE, "factory-configuration");
    public static final QName CONFIGURATION_QNAME         = new QName(NAMESPACE, "configuration");
    public static final QName CONFIGADMIN_QNAME           = new QName(NAMESPACE, "configadmin");
    public static final QName PID_ATTRIBUTE_QNAME         = new QName("pid");
    public static final QName ID_ATTRIBUTE_QNAME          = new QName("http://www.w3.org/XML/1998/namespace", "id");
    public static final QName NAME_ATTRIBUTE_QNAME        = new QName("name");

    private final XMLInputFactory factory;

    public DefaultConfigAdminParser() {
        this(XMLInputFactory.newInstance());
    }

    public DefaultConfigAdminParser(final XMLInputFactory factory) {
        this.factory = factory;
    }

    @Override
    public ConfigAdmin parse(final URL url) throws XMLStreamException {
        InputStream is = null;
        try {
            is = url.openStream();
            return parse(factory.createXMLEventReader(new InputStreamReader(is)));
        } catch (IOException e) {
            throw new XMLStreamException("Cannot open stream for " + url, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignored
                }
            }
        }
    }

    @Override
    public ConfigAdmin parse(XMLEventReader reader) throws XMLStreamException {
        ConfigAdmin configAdmin = new ConfigAdmin();
        while(reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            switch (event.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    StartElement element = event.asStartElement();
                    // Got a <configadmin> node ?
                    if (CONFIGADMIN_QNAME.equals(element.getName())) {
                        readConfigAdmin(reader, configAdmin);
                    }
            }
        }

        return configAdmin;

    }

    private void readConfigAdmin(XMLEventReader reader,
                                 ConfigAdmin configAdmin)
            throws XMLStreamException {

        EventReaderDelegate scoped = new ScopedXMLEventReader(reader);
        while (scoped.hasNext()) {
            XMLEvent event = scoped.nextEvent();
            switch (event.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    StartElement element = event.asStartElement();
                    Attribute pidAttribute = element.getAttributeByName(PID_ATTRIBUTE_QNAME);
                    if (CONFIGURATION_QNAME.equals(element.getName())) {
                        ConfigurationInfo info = new ConfigurationInfo(pidAttribute.getValue());
                        readConfiguration(reader, info);
                        configAdmin.add(info);
                    }
                    if (FACTORY_CONFIGURATION_QNAME.equals(element.getName())) {
                        Attribute idAttribute = element.getAttributeByName(ID_ATTRIBUTE_QNAME);
                        String id = (idAttribute == null) ? null : idAttribute.getValue();
                        ConfigurationInfo info = new ConfigurationInfo(pidAttribute.getValue(), id, true);
                        readConfiguration(reader, info);
                        configAdmin.add(info);
                    }
            }
        }

    }

    private void readConfiguration(XMLEventReader reader, ConfigurationInfo info) throws XMLStreamException {
        ScopedXMLEventReader scoped = new ScopedXMLEventReader(reader);
        while (scoped.hasNext()) {
            XMLEvent event = scoped.nextEvent();
            switch (event.getEventType()) {
                case XMLEvent.START_ELEMENT:
                    StartElement element = event.asStartElement();
                    if (PROPERTY_QNAME.equals(element.getName())) {
                        String name = element.getAttributeByName(NAME_ATTRIBUTE_QNAME).getValue();
                        String value = scoped.getElementText();
                        info.addProperty(name, value);
                        // Need to manually decrease depth since getElementText() is moving the cursor without notifying the reader
                        scoped.decrease();
                    }
            }
        }
    }

}
