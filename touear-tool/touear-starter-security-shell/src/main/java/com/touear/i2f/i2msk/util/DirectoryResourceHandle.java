package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 *
 */
class DirectoryResourceHandle extends AbstractResourceHandle {

    private final String name;

    private final File file;

    private final Manifest manifest;

    private final URL url;

    private final URL codeSource;

    DirectoryResourceHandle(String name, File file, File codeSource, Manifest manifest) throws MalformedURLException {
        this.name = name;
        this.file = file;
        this.codeSource = codeSource.toURI().toURL();
        this.manifest = manifest;
        url = file.toURI().toURL();
    }

    @Override
    public String getName() {
        return name;
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
        return file.isDirectory();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (file.isDirectory()) {
            return new EmptyInputStream();
        }
        return new FileInputStream(file);
    }

    @Override
    public int getContentLength() {
        if (file.isDirectory() || file.length() > Integer.MAX_VALUE) {
            return -1;
        } else {
            return (int) file.length();
        }
    }

    @Override
    public Manifest getManifest() throws IOException {
        return manifest;
    }

    @Override
    public Attributes getAttributes() throws IOException {
        if (manifest == null) {
            return null;
        }
        return manifest.getAttributes(getName());
    }

    /**
     * Always return null. This could be implementd by verifing the signatures
     * in the manifest file against the actual file, but we don't need this
     * right now.
     *
     * @return null
     */
    @Override
    public Certificate[] getCertificates() {
        return null;
    }

    private static final class EmptyInputStream extends InputStream {

        @Override
        public int read() {
            return -1;
        }

        @Override
        public int read(byte[] b) {
            return -1;
        }

        @Override
        public int read(byte[] b, int off, int len) {
            return -1;
        }

        @Override
        public long skip(long n) {
            return 0;
        }

        @Override
        public int available() {
            return 0;
        }

        @Override
        public void close() {
            // empty
        }

        @Override
        public void mark(int readlimit) {
            // empty
        }

        @Override
        public void reset() {
            // empty
        }

        @Override
        public boolean markSupported() {
            return false;
        }
    }
}
