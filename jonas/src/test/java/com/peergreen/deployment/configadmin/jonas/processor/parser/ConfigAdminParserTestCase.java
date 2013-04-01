package com.peergreen.deployment.configadmin.jonas.processor.parser;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;

/**
 * User: guillaume
 * Date: 30/03/13
 * Time: 09:17
 */
public class ConfigAdminParserTestCase {

    private XMLInputFactory factory;

    @BeforeMethod
    public void setUp() throws Exception {
        factory = XMLInputFactory.newFactory();
    }

    public XMLEventReader createReader(String name) throws Exception {
        return factory.createXMLEventReader(getClass().getResourceAsStream(name));
    }

    @Test
    public void testNormalParsing() throws Exception {
        ConfigAdminParser parser = new ConfigAdminParser();
        ConfigAdmin ca = parser.parse(createReader("/test-configadmin-parsing.xml"));

        assertNotNull(ca);
        assertEquals(ca.getInfos().size(), 2);
    }

    @Test
    public void testExtraNamespaceParsingIsIgnored() throws Exception {
        ConfigAdminParser parser = new ConfigAdminParser();
        ConfigAdmin ca = parser.parse(createReader("/test-configadmin-extra-namespaces.xml"));

        assertNotNull(ca);
        assertEquals(ca.getInfos().size(), 0);
    }
}
