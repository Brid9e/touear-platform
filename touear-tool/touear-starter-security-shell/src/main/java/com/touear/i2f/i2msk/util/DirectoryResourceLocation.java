package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class DirectoryResourceLocation extends AbstractURLResourceLocation {

    private static final Logger logger = Logger.getLogger(DirectoryResourceLocation.class.getName());

    private final File baseDir;

    private boolean manifestLoaded = false;

    private Manifest manifest;

    public DirectoryResourceLocation(File baseDir) throws MalformedURLException {
        super(baseDir.toURI().toURL());
        this.baseDir = baseDir;
    }

    @Override
    public ResourceHandle getResourceHandle(String resourceName) {
        File file = new File(baseDir, resourceName);
        if (!file.exists() || !isLocal(file)) {
            return null;
        }
        try {
            return new DirectoryResourceHandle(resourceName, file, baseDir, getManifestSafe());
        } catch (MalformedURLException e) {
            logger.log(Level.FINE, e.getMessage(), e);
            return null;
        }
    }

    private boolean isLocal(File file) {
        try {
            String base = baseDir.getCanonicalPath();
            String relative = file.getCanonicalPath();
            return relative.startsWith(base);
        } catch (IOException e) {
            logger.log(Level.FINE, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Manifest getManifest() throws IOException {
        if (!manifestLoaded) {
            File manifestFile = new File(baseDir, "META-INF/MANIFEST.MF");
            if (manifestFile.isFile() && manifestFile.canRead()) {
            	FileInputStream in = new FileInputStream(manifestFile);
                try{
                    manifest = new Manifest(in);
                }finally{
                	in.close();
                }
            }
            manifestLoaded = true;
        }
        return manifest;
    }

    private Manifest getManifestSafe() {
        Manifest m = null;
        try {
            m = getManifest();
        } catch (IOException e) {
            logger.log(Level.FINE, e.getMessage(), e);
        }
        return m;
    }
}
