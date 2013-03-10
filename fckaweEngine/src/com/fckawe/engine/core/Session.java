package com.fckawe.engine.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.grafix.BitmapsLogic;
import com.fckawe.engine.grafix.Fonts;
import com.fckawe.engine.grafix.FontsLogic;

public class Session {
	
	public enum LoggerType {
		MAIN, CONFIG, HEART, GRAFIX, SOUND
	}
	
	private static Session session;
	private Configuration configuration;
	private Heart heart;
	private Fonts fonts;
	private FontsLogic fontsLogic;
	private Bitmaps bitmaps;
	private BitmapsLogic bitmapsLogic;
	private final Lang lang = new Lang();
	
	public static Session newSession(final Heart heart) {
		session = new Session(heart);
		session.init();
		return session;
	}
	
	public static Session getSession() {
		return session;
	}
	
	private Session(final Heart heart) {
		this.heart = heart;
	}
	
	protected void init() {
		configuration = new Configuration();
		bitmaps = new Bitmaps();
		bitmapsLogic = new BitmapsLogic();
		fonts = new Fonts();
		fonts.loadFonts();
		fontsLogic = new FontsLogic();
	}
	
	public Heart getHeart() {
		return heart;
	}
	
	public Lang getLang() {
		return lang;
	}
	
	public Logger getLogger(final LoggerType type) {
		return LoggerFactory.getLogger(type.toString());
	}
	
	public Logger getMainLogger() {
		return getLogger(LoggerType.MAIN);
	}
	
	public Logger getConfigLogger() {
		return getLogger(LoggerType.CONFIG);
	}
	
	public Logger getHeartLogger() {
		return getLogger(LoggerType.HEART);
	}
	
	public Logger getGrafixLogger() {
		return getLogger(LoggerType.GRAFIX);
	}
	
	public String getEngineName() {
		return "fckaweEngine";
	}
	
	public Fonts getFonts() {
		return fonts;
	}
	
	public FontsLogic getFontsLogic() {
		return fontsLogic;
	}
	
	public Bitmaps getBitmaps() {
		return bitmaps;
	}
	
	public BitmapsLogic getBitmapsLogic() {
		return bitmapsLogic;
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}

}
