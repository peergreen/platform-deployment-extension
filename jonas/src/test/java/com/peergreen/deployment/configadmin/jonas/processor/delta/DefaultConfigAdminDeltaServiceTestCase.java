package com.peergreen.deployment.configadmin.jonas.processor.delta;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.peergreen.deployment.configadmin.jonas.ConfigAdmin;
import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;

/**
 * User: guillaume
 * Date: 11/06/13
 * Time: 15:04
 */
public class DefaultConfigAdminDeltaServiceTestCase {

    private ConfigAdminDeltaService service;
    private ConfigAdmin previous;
    private ConfigAdmin actual;

    @BeforeMethod
    public void setUp() throws Exception {
        service = new DefaultConfigAdminDeltaService();
        previous = new ConfigAdmin();
        actual = new ConfigAdmin();

    }

    @Test
    public void testConfigurationUnchanged() throws Exception {
        previous.add(new ConfigurationInfo("one"));
        actual.add(new ConfigurationInfo("one"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        Delta delta = deltas.get(0);

        assertSame(delta.getActual(), find("one", actual));
        assertSame(delta.getPrevious(), find("one", previous));
        assertEquals(delta.getKind(), Kind.UNCHANGED);
    }

    @Test
    public void testConfigurationAdded() throws Exception {
        actual.add(new ConfigurationInfo("one"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        Delta delta = deltas.get(0);

        assertSame(delta.getActual(), find("one", actual));
        assertNull(delta.getPrevious());
        assertEquals(delta.getKind(), Kind.ADDED);
    }

    @Test
    public void testConfigurationAdded2() throws Exception {
        previous.add(new ConfigurationInfo("one"));
        actual.add(new ConfigurationInfo("one"));
        actual.add(new ConfigurationInfo("two"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.UNCHANGED)));
        assertTrue(deltas.contains(new Delta(null,
                                             find("two", actual),
                                             Kind.ADDED)));
    }

    @Test
    public void testConfigurationRemoved() throws Exception {
        previous.add(new ConfigurationInfo("one"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             null,
                                             Kind.REMOVED)));
    }

    @Test
    public void testConfigurationRemoved2() throws Exception {
        previous.add(new ConfigurationInfo("one"));
        previous.add(new ConfigurationInfo("two"));
        actual.add(new ConfigurationInfo("two"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             null,
                                             Kind.REMOVED)));
        assertTrue(deltas.contains(new Delta(find("two", previous),
                                             find("two", actual),
                                             Kind.UNCHANGED)));
    }

    @Test
    public void testConfigurationChangedWithNewProperties() throws Exception {
        previous.add(new ConfigurationInfo("one").addProperty("a", "b"));
        actual.add(new ConfigurationInfo("one")
                           .addProperty("a", "b")
                           .addProperty("c", "d"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.CHANGED)));
    }

    @Test
    public void testConfigurationChangedWithMissingProperties() throws Exception {
        previous.add(new ConfigurationInfo("one").addProperty("a", "b"));
        actual.add(new ConfigurationInfo("one"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.CHANGED)));
    }

    @Test
    public void testConfigurationChangedWithModifiedProperties() throws Exception {
        previous.add(new ConfigurationInfo("one").addProperty("a", "b"));
        actual.add(new ConfigurationInfo("one")
                           .addProperty("a", "d"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.CHANGED)));
    }

    @Test
    public void testFactoryConfigurationWithNoId() throws Exception {

        // No ID means no tracking is possible so we expect to have a REMOVED and an ADDED Delta

        previous.add(new ConfigurationInfo("one", true));
        actual.add(new ConfigurationInfo("one", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(previous.getInfos().iterator().next(),
                                             null,
                                             Kind.REMOVED)));
        assertTrue(deltas.contains(new Delta(null,
                                             actual.getInfos().iterator().next(),
                                             Kind.ADDED)));
    }

    @Test
    public void testFactoryConfigurationWithIdAndSamePid() throws Exception {

        // No ID means no tracking is possible so we expect to have a REMOVED and an ADDED Delta

        previous.add(new ConfigurationInfo("one", "1", true));
        previous.add(new ConfigurationInfo("one", "2", true));
        actual.add(new ConfigurationInfo("one", "1", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("1", previous),
                                             find("1", actual),
                                             Kind.UNCHANGED)));
        assertTrue(deltas.contains(new Delta(find("2", previous),
                                             null,
                                             Kind.REMOVED)));
    }

    @Test
    public void testFactoryConfigurationWherePidHasChanged() throws Exception {

        // No ID means no tracking is possible so we expect to have a REMOVED and an ADDED Delta

        previous.add(new ConfigurationInfo("one", "1", true));
        actual.add(new ConfigurationInfo("two", "1", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("1", previous),
                                             null,
                                             Kind.REMOVED)));
        assertTrue(deltas.contains(new Delta(null,
                                             find("1", actual),
                                             Kind.ADDED)));
    }

    @Test
    public void testFactoryConfigurationWhereTypeHasChanged() throws Exception {

        // No ID means no tracking is possible so we expect to have a REMOVED and an ADDED Delta

        previous.add(new ConfigurationInfo("one", "1", true));
        actual.add(new ConfigurationInfo("one", "1", false));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("1", previous),
                                             null,
                                             Kind.REMOVED)));
        assertTrue(deltas.contains(new Delta(null,
                                             find("1", actual),
                                             Kind.ADDED)));
    }

    @Test
    public void testFactoryConfigurationUnchanged() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true));
        actual.add(new ConfigurationInfo("one", "one", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        Delta delta = deltas.get(0);

        assertSame(delta.getActual(), find("one", actual));
        assertSame(delta.getPrevious(), find("one", previous));
        assertEquals(delta.getKind(), Kind.UNCHANGED);
    }

    @Test
    public void testFactoryConfigurationAdded() throws Exception {
        actual.add(new ConfigurationInfo("one", "one", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        Delta delta = deltas.get(0);

        assertSame(delta.getActual(), find("one", actual));
        assertNull(delta.getPrevious());
        assertEquals(delta.getKind(), Kind.ADDED);
    }

    @Test
    public void testFactoryConfigurationAdded2() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true));
        actual.add(new ConfigurationInfo("one", "one", true));
        actual.add(new ConfigurationInfo("two", "two", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.UNCHANGED)));
        assertTrue(deltas.contains(new Delta(null,
                                             find("two", actual),
                                             Kind.ADDED)));
    }

    @Test
    public void testFactoryConfigurationRemoved() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             null,
                                             Kind.REMOVED)));
    }

    @Test
    public void testFactoryConfigurationRemoved2() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true));
        previous.add(new ConfigurationInfo("two", "two", true));
        actual.add(new ConfigurationInfo("two", "two", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 2);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             null,
                                             Kind.REMOVED)));
        assertTrue(deltas.contains(new Delta(find("two", previous),
                                             find("two", actual),
                                             Kind.UNCHANGED)));
    }

    @Test
    public void testFactoryConfigurationChangedWithNewProperties() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true).addProperty("a", "b"));
        actual.add(new ConfigurationInfo("one", "one", true)
                           .addProperty("a", "b")
                           .addProperty("c", "d"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.CHANGED)));
    }

    @Test
    public void testFactoryConfigurationChangedWithMissingProperties() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true).addProperty("a", "b"));
        actual.add(new ConfigurationInfo("one", "one", true));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.CHANGED)));
    }

    @Test
    public void testFactoryConfigurationChangedWithModifiedProperties() throws Exception {
        previous.add(new ConfigurationInfo("one", "one", true).addProperty("a", "b"));
        actual.add(new ConfigurationInfo("one", "one", true)
                           .addProperty("a", "d"));

        List<Delta> deltas = service.delta(previous, actual);

        assertEquals(deltas.size(), 1);
        assertTrue(deltas.contains(new Delta(find("one", previous),
                                             find("one", actual),
                                             Kind.CHANGED)));
    }


    private static ConfigurationInfo find(String id, ConfigAdmin ca) throws Exception {
        for (ConfigurationInfo info : ca.getInfos()) {
            if (id.equals(info.getId())) {
                return info;
            }
        }
        throw new Exception("Cannot find ConfigurationInfo with id " + id);
    }
}
