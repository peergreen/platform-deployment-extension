package com.peergreen.deployment.configadmin.jonas.processor.parser;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;

/**
 * User: guillaume
 * Date: 30/03/13
 * Time: 09:17
 */
public class DefaultConfigAdminParserTestCase {

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
        ConfigAdminParser parser = new DefaultConfigAdminParser();
        ConfigAdmin ca = parser.parse(createReader("/test-configadmin-parsing.xml"));

        assertNotNull(ca);
        assertEquals(ca.getInfos().size(), 2);
    }

    @Test
    public void testExtraNamespaceParsingIsIgnored() throws Exception {
        ConfigAdminParser parser = new DefaultConfigAdminParser();
        ConfigAdmin ca = parser.parse(createReader("/test-configadmin-extra-namespaces.xml"));

        assertNotNull(ca);
        assertEquals(ca.getInfos().size(), 0);
    }

    @Test
    public void testIdParsing() throws Exception {
        ConfigAdminParser parser = new DefaultConfigAdminParser();
        ConfigAdmin ca = parser.parse(createReader("/test-configadmin-id-parsing.xml"));

        assertNotNull(ca);
        assertEquals(ca.getInfos().size(), 3);
        Collection<ConfigurationInfo> infos = findPid(ca.getInfos(), "a.factory");
        assertEquals(infos.size(), 2);

        assertNotNull(findId(infos, "one").iterator().next());
        assertNotNull(findId(infos, "two").iterator().next());

    }

    private Collection<ConfigurationInfo> findPid(final Collection<ConfigurationInfo> ca, final String pid) {
        Collection<ConfigurationInfo> found = new HashSet<>();
        for (ConfigurationInfo info : ca) {
            if (pid.equals(info.getPid())) {
                found.add(info);
            }
        }
        return found;
    }

    private Collection<ConfigurationInfo> findId(final Collection<ConfigurationInfo> ca, final String pid) {
        Collection<ConfigurationInfo> found = new HashSet<>();
        for (ConfigurationInfo info : ca) {
            if (pid.equals(info.getId())) {
                found.add(info);
            }
        }
        return found;
    }
}
