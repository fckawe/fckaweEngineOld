package com.fckawe.engine.logic;

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
	
	private Heart heart;
	
	private Fonts fonts;
	
	private FontsLogic fontsLogic;
	
	private Bitmaps bitmaps;
	
	private BitmapsLogic bitmapsLogic;
	
	public static void init(final Heart heart) {
		session = new Session(heart);
		session.getFonts().loadFonts();
	}
	
	public static Session getSession() {
		return session;
	}
	
	private Session(final Heart heart) {
		this.heart = heart;
		fonts = new Fonts();
		fontsLogic = new FontsLogic();
		bitmaps = new Bitmaps();
		bitmapsLogic = new BitmapsLogic();
	}
	
	public Heart getHeart() {
		return heart;
	}
	
	public Logger getLogger(final LoggerType type) {
		return LoggerFactory.getLogger(type.toString());
	}
	
	public Logger getMainLogger() {
		return getLogger(LoggerType.MAIN);
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

}
