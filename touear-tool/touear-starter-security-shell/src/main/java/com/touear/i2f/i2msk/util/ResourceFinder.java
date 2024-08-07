package com.touear.i2f.i2msk.util;

import java.net.URL;
import java.util.Enumeration;

/**
 * Abstraction of resource searching policy. Given resource name, the resource
 * finder performs implementation-specific lookup, and, if it is able to locate
 * the resource, returns the {@link ResourceHandle handle(s)} or URL(s)
 * of it.
 */
public interface ResourceFinder {

    /**
     * Find the resource by name and return URL of it if found.
     *
     * @param name the resource name
     * @return resource URL or null if resource was not found
     */
    URL findResource(String name);

    /**
     * Find all resources with given name and return enumeration of their URLs.
     *
     * @param name the resource name
     * @return enumeration of resource URLs (possibly empty).
     */
    Enumeration<URL> findResources(String name);

    /**
     * Get the resource by name and, if found, open connection to it and return
     * the {@link ResourceHandle handle} of it.
     *
     * @param name the resource name
     * @return resource handle or null if resource was not found
     */
    ResourceHandle getResource(String name);
}
