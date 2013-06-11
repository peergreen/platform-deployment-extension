package com.peergreen.deployment.configadmin.jonas.processor.delta;

import com.peergreen.deployment.configadmin.jonas.ConfigurationInfo;

/**
 * User: guillaume
 * Date: 11/06/13
 * Time: 12:04
 */
public class Delta {
    private final ConfigurationInfo previous;
    private final ConfigurationInfo actual;
    private final Kind kind;

    public Delta(final ConfigurationInfo previous,
                 final ConfigurationInfo actual,
                 final Kind kind) {
        this.previous = previous;
        this.actual = actual;
        this.kind = kind;
    }

    public ConfigurationInfo getPrevious() {
        return previous;
    }

    public ConfigurationInfo getActual() {
        return actual;
    }

    public Kind getKind() {
        return kind;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Delta delta = (Delta) o;

        if (actual != null ? !actual.equals(delta.actual) : delta.actual != null) {
            return false;
        }
        if (kind != delta.kind) {
            return false;
        }
        if (previous != null ? !previous.equals(delta.previous) : delta.previous != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = previous != null ? previous.hashCode() : 0;
        result = 31 * result + (actual != null ? actual.hashCode() : 0);
        result = 31 * result + kind.hashCode();
        return result;
    }
}
