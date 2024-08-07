package com.touear.i2f.i2msk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 *
 */
public abstract class AbstractResourceHandle implements ResourceHandle {

    @Override
    public byte[] getBytes() throws IOException {
    	InputStream inputStream = getInputStream();
        try {
            byte[] buffer = new byte[4096];
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (int count = inputStream.read(buffer); count >= 0; count = inputStream.read(buffer)) {
                out.write(buffer, 0, count);
            }
            return out.toByteArray();
        }finally{
        	inputStream.close();
        }
    }

    @Override
    public Manifest getManifest() throws IOException {
        return null;
    }

    @Override
    public Certificate[] getCertificates() {
        return null;
    }

    @Override
    public Attributes getAttributes() throws IOException {
        Manifest m = getManifest();
        if (m == null) {
            return null;
        }
        String entry = getUrl().getFile();
        return m.getAttributes(entry);
    }

    @Override
    public void close() {
    }

    @Override
    public String toString() {
        return "[" + getName() + ": " + getUrl() + "; code source: " + getCodeSourceUrl() + "]";
    }
}
