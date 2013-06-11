package com.peergreen.deployment.configadmin.jonas.processor.parser;

import java.net.URL;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;

/**
 * User: guillaume
 * Date: 11/06/13
 * Time: 10:56
 */
public interface ConfigAdminParser {
    String NAMESPACE = "http://jonas.ow2.org/ns/configadmin/1.0";

    ConfigAdmin parse(URL url) throws XMLStreamException;
    ConfigAdmin parse(XMLEventReader reader) throws XMLStreamException;
}
