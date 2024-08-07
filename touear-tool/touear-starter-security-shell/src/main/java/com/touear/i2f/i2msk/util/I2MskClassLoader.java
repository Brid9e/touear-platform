package com.touear.i2f.i2msk.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class I2MskClassLoader extends ClassLoader{
	
	URIClassLoader loader;
	Map<String, Class<?>> classMap=new HashMap<String, Class<?>>();
	public I2MskClassLoader() {
		super(I2MskClassLoader.class.getClassLoader());
		loader=new URIClassLoader();	
		loader.addURI(getJar("/bcprov-ext-jdk15on-1.61.fzhjar").toURI());
		loader.addURI(getJar("/bcprov-jdk15on-1.61.fzhjar").toURI());
		loader.addURI(getJar("/hutool-all-4.5.9.fzhjar").toURI());
		loader.addURI(getJar("/securityKeyboard.fzhjar").toURI());
//		loader.addURI(getJar("/securityKeyboard.i2mjar").toURI());
	}
	public File getJar(String jarName){
		File f=new File(System.getProperty("java.io.tmpdir"),jarName);
		if(f.exists()){
			f.delete();
		}
		try {
			FileOutputStream outputStream=new FileOutputStream(f);
			InputStream inputStream = I2MskClassLoader.class.getResourceAsStream(jarName);
			byte[] buffer = new byte[1024];  
		    int len = -1;  
		    while ((len = inputStream.read(buffer)) != -1) {  
		    	outputStream.write(buffer, 0, len);  
		    }
		    outputStream.close();
		    inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}
	@Override
	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		return findClass(name);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return findClass(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if(!classMap.containsKey(name)){
			classMap.put(name, loader.findClass(name));
		}
		Class<?> c = classMap.get(name);
		if(c == null){
			return I2MskClassLoader.class.getClassLoader().loadClass(name);	
		}else{
			return c;
		}
	}
}
