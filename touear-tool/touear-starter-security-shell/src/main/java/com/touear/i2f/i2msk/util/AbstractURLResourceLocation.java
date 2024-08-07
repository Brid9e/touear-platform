package com.touear.i2f.i2msk.util;

import java.net.URL;

/**
 *
 */
public abstract class AbstractURLResourceLocation implements ResourceLocation {

    private final URL codeSource;

    public AbstractURLResourceLocation(URL codeSource) {
        this.codeSource = codeSource;
    }

    @Override
    public final URL getCodeSource() {
        return codeSource;
    }

    @Override
    public void close() {
    }

    @Override
    public final boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass()) &&
                toString().equals(o.toString());
    }

    @Override
    public final int hashCode() {
        return codeSource.toString().hashCode();
    }

    @Override
    public final String toString() {
        return "[" + getClass().getName() + ": " + codeSource + "]";
    }
}
