package com.peergreen.deployment.configadmin.jonas.processor.parser;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.EventReaderDelegate;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;

/**
 * User: guillaume
 * Date: 30/03/13
 * Time: 09:01
 */
public class ConfigAdminParser {
    public static final String NAMESPACE = "http://jonas.ow2.org/ns/configadmin/1.0";
    public static final QName PROPERTY_QNAME              = new QName(NAMESPACE, "property");
    public static final QName FACTORY_CONFIGURATION_QNAME = new QName(NAMESPACE, "factory-configuration");
    public static final QName CONFIGURATION_QNAME         = new QName(NAMESPACE, "configuration");
    public static final QName CONFIGADMIN_QNAME           = new QName(NAMESPACE, "configadmin");
    public static final QName PID_ATTRIBUTE_QNAME         = new QName("pid");
    public static final QName NAME_ATTRIBUTE_QNAME        = new QName("name");

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
                    if (ConfigAdminParser.CONFIGURATION_QNAME.equals(element.getName())) {
                        ConfigurationInfo info = new ConfigurationInfo(element.getAttributeByName(ConfigAdminParser.PID_ATTRIBUTE_QNAME).getValue());
                        readConfiguration(reader, info);
                        configAdmin.add(info);
                    }
                    if (ConfigAdminParser.FACTORY_CONFIGURATION_QNAME.equals(element.getName())) {
                        ConfigurationInfo info = new ConfigurationInfo(element.getAttributeByName(ConfigAdminParser.PID_ATTRIBUTE_QNAME).getValue(), true);
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
                    if (ConfigAdminParser.PROPERTY_QNAME.equals(element.getName())) {
                        String name = element.getAttributeByName(ConfigAdminParser.NAME_ATTRIBUTE_QNAME).getValue();
                        String value = scoped.getElementText();
                        info.addProperty(name, value);
                        // Need to manually decrease depth since getElementText() is moving the cursor without notifying the reader
                        scoped.decrease();
                    }
            }
        }
    }

}
