package com.touear.i2f.i2msk.util;

import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 */
public class ResourceEnumeration implements Enumeration<URL> {

    private final String resourceName;
    private Iterator<ResourceLocation> iterator;
    private URL next;

    public ResourceEnumeration(Collection<ResourceLocation> resourceLocations, String resourceName) {
        this.iterator = resourceLocations.iterator();
        this.resourceName = resourceName;
    }

    @Override
    public boolean hasMoreElements() {
        fetchNext();
        return next != null;
    }

    @Override
    public URL nextElement() {
        fetchNext();
        // save next into a local variable and clear the next field
        URL url = this.next;
        this.next = null;
        // if we didn't have a next throw an exception
        if (url == null) {
            throw new NoSuchElementException();
        }
        return url;
    }

    private void fetchNext() {
        if (iterator == null) {
            return;
        }
        if (next != null) {
            return;
        }
        try {
            while (iterator.hasNext()) {
                ResourceLocation resourceLocation = iterator.next();
                ResourceHandle resourceHandle = resourceLocation.getResourceHandle(resourceName);
                if (resourceHandle != null) {
                    next = resourceHandle.getUrl();
                    return;
                }
            }
            // no more elements
            // clear the iterator so it can be GCed
            iterator = null;
        } catch (IllegalStateException e) {
            // Jar file was closed... this means the resource finder was destroyed
            // clear the iterator so it can be GCed
            iterator = null;
            throw e;
        }
    }
}
