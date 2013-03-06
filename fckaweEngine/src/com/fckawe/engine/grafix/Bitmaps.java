package com.fckawe.engine.grafix;

import java.util.HashMap;
import java.util.Map;

public class Bitmaps {
	
	private final Map<String, Bitmap[][]> bitmaps = new HashMap<String, Bitmap[][]>();
	
	public void clear() {
		bitmaps.clear();
	}
	
	public void putBitmap(final String name, final Bitmap bitmap) {
		bitmaps.put(name, new Bitmap[][] {{bitmap}});
	}
	
	public void putBitmapSprite(final String name, final Bitmap[][] sprite) {
		bitmaps.put(name, sprite);
	}
	
	public Bitmap getBitmap(final String name) {
		return bitmaps.get(name)[0][0];
	}
	
	public Bitmap[][] getBitmapSprite(final String name) {
		return bitmaps.get(name);
	}

}
