package com.touear.i2f.i2msk.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 */
class JarResourceHandle extends AbstractResourceHandle {

    private final JarFile jarFile;

    private final JarEntry jarEntry;

    private final URL url;

    private final URL codeSource;

    JarResourceHandle(JarFile jarFile, JarEntry jarEntry, URL codeSource) throws MalformedURLException {
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
        this.url = JarFileUrlStreamHandler.createURL(jarFile, jarEntry, codeSource);
        this.codeSource = codeSource;
    }

    @Override
    public String getName() {
        return jarEntry.getName();
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public URL getCodeSourceUrl() {
        return codeSource;
    }

    @Override
    public boolean isDirectory() {
        return jarEntry.isDirectory();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return jarFile.getInputStream(jarEntry);
    }

    @Override
    public int getContentLength() {
        return (int) jarEntry.getSize();
    }

    @Override
    public Manifest getManifest() throws IOException {
        return jarFile.getManifest();
    }

    @Override
    public Attributes getAttributes() throws IOException {
        return jarEntry.getAttributes();
    }

    @Override
    public Certificate[] getCertificates() {
        return jarEntry.getCertificates();
    }
}
