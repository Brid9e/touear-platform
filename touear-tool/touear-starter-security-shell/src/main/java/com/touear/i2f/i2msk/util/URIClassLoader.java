package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.cert.Certificate;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

/**
 * Equivalent of java.net.URLClassloader but without bugs related to ill-formed
 * URLs and with customizable JAR caching policy. The standard URLClassLoader
 * accepts URLs containing spaces and other characters which are forbidden in
 * the URI syntax, according to the RFC 2396. As a workaround to this problem,
 * Java escapes and un-escapes URLs in various arbitrary places; however, this
 * is inconsistent and leads to numerous problems with URLs referring to local
 * files with spaces in the path. SUN acknowledges the problem, but refuses to
 * modify the behavior for compatibility reasons; see Java Bug Parade 4273532,
 * 4466485.
 * Additionally, the JAR caching policy used by URLClassLoader is system-wide
 * and inflexible: once downloaded JAR files are never re-downloaded, even if
 * one creates a fresh instance of the class loader that happens to have the
 * same URL in its search path. In fact, that policy is a security
 * vulnerability: it is possible to crash any URL class loader, thus affecting
 * potentially separate part of the system, by creating URL connection to one of
 * the URLs of that class loader search path and closing the associated JAR
 * file. See Java Bug Parade 4405789, 4388666, 4639900.
 * This class avoids these problems by 1) using URIs instead of URLs for the
 * search path (thus enforcing strict syntax conformance and defining precise
 * escaping semantics), and 2) using custom URLStreamHandler which ensures
 * per-classloader JAR caching policy.
 */
public final class URIClassLoader extends URLClassLoader {

    private final URIResourceFinder finder = new URIResourceFinder();

    private final AccessControlContext acc;

    /**
     * Creates URIClassLoader.
     */
    public URIClassLoader() {
        this(null);
    }

    /**
     * Creates URIClassLoader with the specified parent class loader.
     *
     * @param parent the parent class loader.
     */
    public URIClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
        this.acc = AccessController.getContext();
    }

    /**
     * Add specified URI at the end of the search path.
     *
     * @param uri the URI to add
     * @return the class loader
     */
    public URIClassLoader addURI(URI uri) {
        finder.addURI(uri);
        return this;
    }

    public URI[] getURIs() {
        return finder.getURIs();
    }

    /**
     * Finds and loads the class with the specified name.
     *
     * @param name the name of the class
     * @return the resulting class
     * @throws ClassNotFoundException if the class could not be found
     */
    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
    	Class<?> c1 = AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {

			@Override
			public Class<?> run() {
				try {
					ResourceHandle h = finder.getResource(name.replace('.', '/').concat(".class"));
	                if (h == null) {
	                	Class<?> c = URIClassLoader.class.getClassLoader().loadClass(name);
	                	if(c == null){
	                		throw new ClassNotFoundException(name);
	                	}else{
	                		return c;
	                	}
	                } else {
	                    return defineClass(name, h);
	                }
				} catch (Exception e) {
					return null;
				}
				
			}
		},acc);
    	return c1;
    }

    /**
     * Finds the resource with the specified name.
     *
     * @param name the name of the resource
     * @return a <code>URL</code> for the resource, or <code>null</code> if the
     * resource could not be found.
     */
    @Override
    public URL findResource(final String name) {
        return AccessController.doPrivileged(new PrivilegedAction<URL>() {

			@Override
			public URL run() {
				// TODO Auto-generated method stub
				return finder.findResource(name);
			}
		},acc);
    }

    /**
     * Returns an Enumeration of URLs representing all of the resources having
     * the specified name.
     *
     * @param name the resource name
     * @return an <code>Enumeration</code> of <code>URL</code>s
     * @throws IOException if an I/O exception occurs
     */
    @Override
    public Enumeration<URL> findResources(final String name) throws IOException {
    	
        return AccessController.doPrivileged(new PrivilegedAction<Enumeration<URL>>() {

			@Override
			public Enumeration<URL> run() {
				// TODO Auto-generated method stub
				return finder.findResources(name);
			}
		},acc);
    }

    /**
     * Returns the absolute path name of a native library. The VM invokes this
     * method to locate the native libraries that belong to classes loaded with
     * this class loader. If this method returns
     * <code>null</code>, the VM searches the library along the path specified
     * as the
     * <code>java.library.path</code> property. This method invoke
     * {@link #getLibraryHandle} method to find handle of this library. If the
     * handle is found and its URL protocol is "file", the system-dependent
     * absolute library file path is returned. Otherwise this method returns
     * null.
     * Subclasses can override this method to provide specific approaches in
     * library searching.
     *
     * @param libname the library name
     * @return the absolute path of the native library
     * @see System#loadLibrary(String)
     * @see System#mapLibraryName(String)
     */
    @Override
    protected String findLibrary(String libname) {
        ResourceHandle md = getLibraryHandle(libname);
        if (md == null) {
            return null;
        }
        URL url = md.getUrl();
        if (!"file".equals(url.getProtocol())) {
            return null;
        }
        return new File(URI.create(url.toString())).getPath();
    }

    /**
     * Finds the ResourceHandle object for the resource with the specified name.
     *
     * @param name the name of the resource
     * @return the ResourceHandle of the resource
     */
    private ResourceHandle getResourceHandle(final String name) {
        return AccessController.doPrivileged(new PrivilegedAction<ResourceHandle>() {

			@Override
			public ResourceHandle run() {
				// TODO Auto-generated method stub
				return finder.getResource(name);
			}
		},acc);
        		
    }

    /**
     * Finds the ResourceHandle object for the native library with the specified
     * name. The library name must be '/'-separated path. The last part of this
     * path is substituted by its system-dependent mapping (using
     * {@link System#mapLibraryName(String)} method). Next, the
     * <code>ResourceFinder</code> is used to look for the library as it was
     * ordinary resource. <p>
     * <p/>
     * Subclasses can override this method to provide specific approaches in
     * library searching.
     *
     * @param name the name of the library
     * @return the ResourceHandle of the library
     */
    private ResourceHandle getLibraryHandle(final String name) {
        int idx = name.lastIndexOf('/');
        String path;
        String simplename;
        if (idx == -1) {
            path = "";
            simplename = name;
        } else if (idx == name.length() - 1) { // name.endsWith('/')
            throw new IllegalArgumentException(name);
        } else {
            path = name.substring(0, idx + 1); // including '/'
            simplename = name.substring(idx + 1);
        }
        return getResourceHandle(path + System.mapLibraryName(simplename));
    }

    /**
     * Returns true if the specified package name is sealed according to the
     * given manifest.
     */
    private boolean isPackageSealed(String name, Manifest man) {
        String path = name.replace('.', '/').concat("/");
        Attributes attr = man.getAttributes(path);
        String sealed = null;
        if (attr != null) {
            sealed = attr.getValue(Name.SEALED);
        }
        if (sealed == null && (attr = man.getMainAttributes()) != null) {
            sealed = attr.getValue(Name.SEALED);
        }
        return "true".equalsIgnoreCase(sealed);
    }

    private Class<?> defineClass(String name, ResourceHandle h) throws IOException {
        int i = name.lastIndexOf('.');
        URL url = h.getCodeSourceUrl();
        if (i != -1) { // check package
            String pkgname = name.substring(0, i);
            // check if package already loaded
            Package pkg = getPackage(pkgname);
            Manifest man = h.getManifest();
            if (pkg != null) {
                // package found, so check package sealing
                boolean ok;
                if (pkg.isSealed()) {
                    // verify that code source URLs are the same
                    ok = pkg.isSealed(url);
                } else {
                    // make sure we are not attempting to seal the package
                    // at this code source URL
                    ok = (man == null) || !isPackageSealed(pkgname, man);
                }
                if (!ok) {
                    throw new SecurityException("sealing violation: " + name);
                }
            } else { // package not yet defined
                if (man != null) {
                    definePackage(pkgname, man, url);
                } else {
                    definePackage(pkgname, null, null, null, null, null, null, null);
                }
            }
        }
        // now read the class bytes and define the class
        byte[] b = h.getBytes();
        Certificate[] certs = h.getCertificates();
        CodeSource cs = new CodeSource(url, certs);
        return defineClass(name, b, 0, b.length, cs);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("URIClassLoader:");
        for (URI uri : getURIs()) {
            sb.append("[").append(uri).append("]");
        }
        return sb.toString();
    }
}
