package com.peergreen.deployment.configadmin.jonas.processor;

import java.util.Iterator;
import java.util.List;

import com.peergreen.deployment.configadmin.jonas.processor.delta.Delta;

/**
 * User: guillaume
 * Date: 13/06/13
 * Time: 13:32
 */
public class Deltas implements Iterable<Delta> {
    private final List<Delta> deltas;

    public Deltas(final List<Delta> deltas) {
        this.deltas = deltas;
    }

    @Override
    public Iterator<Delta> iterator() {
        return deltas.iterator();
    }
}
