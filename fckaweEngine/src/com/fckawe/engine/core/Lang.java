package com.fckawe.engine.core;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.fckawe.engine.core.Session.LoggerType;

public class Lang {
	
	public static final String BASE = "FCKAWE_BASE";
	
	private final Map<String, ResourceBundle> bundles =
			new HashMap<String, ResourceBundle>();
	
	public Lang() {
		addResourceBundle(BASE, "lang.fckawe");
	}
	
	public void addResourceBundle(final String name, final String path) {
		ResourceBundle rb = loadResourceBundle(path);
		bundles.put(name, rb);
	}
	
	public String getString(final String key) {
		return getString(BASE, key);
	}
	
	public String getString(final String bundle, final String key) {
		ResourceBundle rb = bundles.get(bundle);
		if(rb == null) {
			return null;
		}
		return rb.getString(key);
	}
	
	private ResourceBundle loadResourceBundle(final String path) {
		try {
			return ResourceBundle.getBundle(path);
		} catch(MissingResourceException e) {
			Session.getSession().getLogger(LoggerType.MAIN).error(
					"Language resource bundle not found!", e);
			return null;
		}
	}

}