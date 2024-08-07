package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class JarResourceLocation extends AbstractURLResourceLocation {

    private static final Logger logger = Logger.getLogger(JarResourceLocation.class.getName());

    private final JarFile jarFile;

    public JarResourceLocation(URL codeSource, File cacheFile) throws IOException {
        super(codeSource);
        this.jarFile = new JarFile(cacheFile);
    }

    @Override
    public ResourceHandle getResourceHandle(String resourceName) {
        JarEntry jarEntry = jarFile.getJarEntry(resourceName);
        if (jarEntry != null) {
            try {
                return new JarResourceHandle(jarFile, jarEntry, getCodeSource());
            } catch (MalformedURLException e) {
                logger.log(Level.FINE, e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public Manifest getManifest() throws IOException {
       return jarFile.getManifest();
    }

    @Override
    public void close() {
        try {
            jarFile.close();
        } catch (Exception e) {
            logger.log(Level.FINE, e.getMessage(), e);
        }
    }
}
