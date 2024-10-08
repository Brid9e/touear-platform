package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
class URIResourceFinder implements ResourceFinder {

    private static final Logger logger = Logger.getLogger(URIResourceFinder.class.getName());

    private final Object lock = new Object();

    private final Set<URI> uris = new LinkedHashSet<URI>();

    private final Map<URI, ResourceLocation> classPath = new LinkedHashMap<URI,ResourceLocation>();

    private final Set<File> watchedFiles = new LinkedHashSet<File>();

    private boolean destroyed = false;


    @Override
    public ResourceHandle getResource(String resourceName) {
        synchronized (lock) {
            if (destroyed) {
                return null;
            }
            Map<URI, ResourceLocation> path = getClassPath();
            for (Map.Entry<URI, ResourceLocation> entry : path.entrySet()) {
                ResourceLocation resourceLocation = entry.getValue();
                ResourceHandle resourceHandle = resourceLocation.getResourceHandle(resourceName);
                if (resourceHandle != null && !resourceHandle.isDirectory()) {
                    return resourceHandle;
                }
            }
        }
        return null;
    }

    @Override
    public URL findResource(String resourceName) {
        synchronized (lock) {
            if (destroyed) {
                return null;
            }
            for (Map.Entry<URI, ResourceLocation> entry : getClassPath().entrySet()) {
                ResourceLocation resourceLocation = entry.getValue();
                ResourceHandle resourceHandle = resourceLocation.getResourceHandle(resourceName);
                if (resourceHandle != null) {
                    return resourceHandle.getUrl();
                }
            }
        }
        return null;
    }

    @Override
    public Enumeration<URL> findResources(String resourceName) {
        synchronized (lock) {
            return new ResourceEnumeration(new ArrayList<ResourceLocation>(getClassPath().values()), resourceName);
        }
    }

    void addURI(URI uri) {
        add(Collections.singletonList(uri));
    }

    URI[] getURIs() {
        synchronized (lock) {
            return uris.toArray(new URI[uris.size()]);
        }
    }

    /**
     * Adds a list of uris to the end of this class loader.
     *
     * @param uris the URLs to add
     */
    private void add(List<URI> uris) {
        synchronized (lock) {
            if (destroyed) {
                throw new IllegalStateException("UriResourceFinder has been destroyed");
            }
            boolean shouldRebuild = this.uris.addAll(uris);
            if (shouldRebuild) {
                rebuildClassPath();
            }
        }
    }

    private Map<URI, ResourceLocation> getClassPath() {
        for (File file : watchedFiles) {
            if (file.canRead()) {
                rebuildClassPath();
                break;
            }
        }
        return classPath;
    }

    /**
     * Rebuilds the entire class path. This class is called when new URIs are
     * added or one of the watched files becomes readable. This method will not
     * open jar files again, but will add any new entries not alredy open to the
     * class path. If any file based uri is does not exist, we will watch for
     * that file to appear.
     */
    private void rebuildClassPath() {
        // copy all of the existing locations into a temp map and clear the class path
        Map<URI, ResourceLocation> existingJarFiles = new LinkedHashMap<URI, ResourceLocation>(classPath);
        classPath.clear();
        LinkedList<URI> locationStack = new LinkedList<URI>(uris);
        try {
            while (!locationStack.isEmpty()) {
                URI uri = locationStack.removeFirst();
                if (classPath.containsKey(uri)) {
                    continue;
                }
                // Check is this URL has already been opened
                ResourceLocation resourceLocation = existingJarFiles.remove(uri);
                // If not opened, cache the uri and wrap it with a resource location
                if (resourceLocation == null) {
                    try {
                        resourceLocation = createResourceLocation(uri.toURL(), cacheUri(uri));
                    } catch (FileNotFoundException e) {
                        logger.log(Level.FINE, e.getMessage(), e);
                        // if this is a file URL, the file doesn't exist yet... watch to see if it appears later
                        if ("file".equals(uri.getScheme())) {
                            File file = new File(uri.getPath());
                            watchedFiles.add(file);
                            continue;

                        }
                    } catch (IOException ignored) {
                        logger.log(Level.FINE, ignored.getMessage(), ignored);
                        // can't seem to open the file... this is most likely a bad jar file
                        // so don't keep a watch out for it because that would require lots of checking
                        // Dain: We may want to review this decision later
                        continue;
                    } catch (UnsupportedOperationException ex) {
                        logger.log(Level.FINE, ex.getMessage(), ex);
                        // the protocol for the JAR file's URL is not supported.  This can occur when
                        // the jar file is embedded in an EAR or CAR file.
                        continue;
                    }
                }
                try {
                    // add the jar to our class path
                    if (resourceLocation != null && resourceLocation.getCodeSource() != null) {
                        classPath.put(resourceLocation.getCodeSource().toURI(), resourceLocation);
                    }
                } catch (URISyntaxException ex) {
                    logger.log(Level.FINE, ex.getMessage(), ex);
                    // ignore
                }
                // push the manifest classpath on the stack (make sure to maintain the order)
                if (resourceLocation != null) {
                    List<URI> manifestClassPath = getManifestClassPath(resourceLocation);
                    locationStack.addAll(0, manifestClassPath);
                }
            }
        } catch (Exception e) {
            destroy();
            throw new java.lang.RuntimeException(e);
        }
        for (ResourceLocation resourceLocation : existingJarFiles.values()) {
            resourceLocation.close();
        }
    }

    private File cacheUri(URI uri) throws IOException {
        if (!"file".equals(uri.getScheme())) {
            throw new UnsupportedOperationException("only local file jars are supported " + uri);
        }
        File file = new File(uri.getPath());
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
        if (!file.canRead()) {
            throw new IOException("file is not readable: " + file.getAbsolutePath());
        }
        return file;
    }

    private ResourceLocation createResourceLocation(URL codeSource, File cacheFile) throws IOException {
        if (!cacheFile.exists()) {
            throw new FileNotFoundException(cacheFile.getAbsolutePath());
        }
        if (!cacheFile.canRead()) {
            throw new IOException("file is not readable: " + cacheFile.getAbsolutePath());
        }
        return cacheFile.isDirectory() ?
                // DirectoryResourceLocation will only return "file" URLs within this directory
                // do not use the DirectoryResourceLocation for non file based uris
                new DirectoryResourceLocation(cacheFile) :
                new JarResourceLocation(codeSource, cacheFile);
    }

    private List<URI> getManifestClassPath(ResourceLocation resourceLocation) {
        List<URI> classPathUrls = new LinkedList<URI>();
        Manifest manifest;
        try {
            // get the manifest, if possible
            manifest = resourceLocation.getManifest();
        } catch (IOException ignored) {
            logger.log(Level.FINE, ignored.getMessage(), ignored);
            // error opening the manifest
            return classPathUrls;
        }
        if (manifest == null) {
            // some locations don't have a manifest
            return classPathUrls;
        }
        // get the class-path attribute, if possible
        String manifestClassPath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
        if (manifestClassPath == null) {
            return classPathUrls;
        }
        // build the uris...
        // the class-path attribute is space delimited
        URL codeSource = resourceLocation.getCodeSource();
        for (StringTokenizer tokenizer = new StringTokenizer(manifestClassPath, " "); tokenizer.hasMoreTokens(); ) {
            String entry = tokenizer.nextToken();
            try {
                // the class path entry is relative to the resource location code source
                URL entryUrl = new URL(codeSource, entry);
                classPathUrls.add(entryUrl.toURI());
            } catch (Exception ignored) {
                logger.log(Level.FINE, ignored.getMessage(), ignored);
                // most likely a poorly named entry
            }
        }
        return classPathUrls;
    }

    private void destroy() {
        synchronized (lock) {
            if (destroyed) {
                return;
            }
            destroyed = true;
            uris.clear();
            for (ResourceLocation resourceLocation : classPath.values()) {
                resourceLocation.close();
            }
            classPath.clear();
        }
    }
}
