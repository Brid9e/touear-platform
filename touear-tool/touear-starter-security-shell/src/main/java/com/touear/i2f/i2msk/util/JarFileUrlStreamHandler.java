package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 */
public class JarFileUrlStreamHandler extends URLStreamHandler {

    private final JarFile jarFile;
    private final JarEntry jarEntry;
    private URL expectedUrl;

    public JarFileUrlStreamHandler(JarFile jarFile, JarEntry jarEntry) {
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
    }

    public static URL createURL(JarFile jarFile, JarEntry jarEntry) throws MalformedURLException {
        return createURL(jarFile, jarEntry, new File(jarFile.getName()).toURI().toURL());
    }

    public static URL createURL(JarFile jarFile, JarEntry jarEntry, URL codeSource) throws MalformedURLException {
        JarFileUrlStreamHandler handler = new JarFileUrlStreamHandler(jarFile, jarEntry);
        URL url = new URL("jar", "", -1, codeSource + "!/" + jarEntry.getName(), handler);
        handler.setExpectedUrl(url);
        return url;
    }

    public void setExpectedUrl(URL expectedUrl) {
        this.expectedUrl = expectedUrl;
    }

    @Override
    public URLConnection openConnection(URL url) throws IOException {
        // the caller copied the URL reusing a stream handler from a previous call
        // compare strings to avoid possible DNS host resolution
        if (expectedUrl.toString().equals(url.toString())) {
            return new JarFileUrlConnection(url, jarFile, jarEntry);
        }
        // the new url is supposed to be within our context, so it must have a jar protocol
        if (!"jar".equals(url.getProtocol())) {
            throw new IllegalArgumentException("unsupported protocol " + url.getProtocol());
        }
        // split the path at "!/" into the file part and entry part
        String path = url.getPath();
        String[] chunks = path.split("!/", 2);
        // if we only got only one chunk, it didn't contain the required "!/" delimiter
        if (chunks.length == 1) {
            throw new MalformedURLException("URL does not contain a '!' character: " + url);
        }
        String file = chunks[0];
        String entryPath = chunks[1];
        // this handler only supports jars on the local file system
        if (!file.startsWith("file:")) {
            // let the system handler deal with this
            return new URL(url.toExternalForm()).openConnection();
        }
        file = file.substring("file:".length());
        // again the new url is supposed to be within our context so it must reference the same jar file
        if (!jarFile.getName().equals(file)) {
            // let the system handler deal with this
            return new URL(url.toExternalForm()).openConnection();
        }
        // get the entry
        JarEntry newEntry = jarFile.getJarEntry(entryPath);
        if (newEntry == null) {
            throw new FileNotFoundException("Entry not found: " + url);
        }
        return new JarFileUrlConnection(url, jarFile, newEntry);
    }
}
