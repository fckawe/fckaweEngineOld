package com.fckawe.engine.adapt;

import java.util.HashMap;
import java.util.Map;

public class FckaweFactory {
	
	private static final Map<String, String> replacements =
			new HashMap<String, String>();
	
	public static Object createInstance(final String className) {
		String classNameNew = className;
		if(replacements.containsKey(className)) {
			classNameNew = replacements.get(className);
		}
		
		try {
			return Class.forName(classNameNew);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Invalid replacement for class " +
				className + ": " + classNameNew, e);
		}
	}

}
