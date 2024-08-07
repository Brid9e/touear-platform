package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.Permission;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 *
 */
public class JarFileUrlConnection extends JarURLConnection {

    private static final URL DUMMY_JAR_URL;

    static {
        try {
            DUMMY_JAR_URL = new URL("jar", "", -1, "file:dummy!/", new URLStreamHandler() {
                @Override
                protected URLConnection openConnection(URL u) {
                    throw new UnsupportedOperationException();
                }
            });
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final URL jarUrl;

    private final JarFile jarFile;

    private final JarEntry jarEntry;

    private final URL jarFileUrl;

    public JarFileUrlConnection(URL jarUrl, JarFile jarFile, JarEntry jarEntry) throws MalformedURLException {
        super(DUMMY_JAR_URL);
        this.jarUrl = jarUrl;
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
        jarFileUrl = new File(jarFile.getName()).toURI().toURL();
    }

    @Override
    public JarFile getJarFile() throws IOException {
        if (getUseCaches()) {
            return jarFile;
        } else {
            return new JarFile(jarFile.getName());
        }
    }

    @Override
    public synchronized void connect() {
        // not used
    }

    @Override
    public URL getJarFileURL() {
        return jarFileUrl;
    }

    @Override
    public String getEntryName() {
        return getJarEntry().getName();
    }

    @Override
    public Manifest getManifest() throws IOException {
        return jarFile.getManifest();
    }

    @Override
    public JarEntry getJarEntry() {
        return getUseCaches() ? jarEntry : jarFile.getJarEntry(jarEntry.getName());
    }

    @Override
    public Attributes getAttributes() throws IOException {
        return getJarEntry().getAttributes();
    }

    @Override
    public Attributes getMainAttributes() throws IOException {
        return getManifest().getMainAttributes();
    }

    @Override
    public Certificate[] getCertificates() throws IOException {
        return getJarEntry().getCertificates();
    }

    @Override
    public URL getURL() {
        return jarUrl;
    }

    @Override
    public int getContentLength() {
        long size = getJarEntry().getSize();
        if (size > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) size;
    }

    @Override
    public long getLastModified() {
        return getJarEntry().getTime();
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        return jarFile.getInputStream(jarEntry);
    }

    @Override
    public Permission getPermission() throws IOException {
        URL url = new File(jarFile.getName()).toURI().toURL();
        return url.openConnection().getPermission();
    }

    @Override
    public String toString() {
        return JarFileUrlConnection.class.getName() + ":" + jarUrl;
    }
}
