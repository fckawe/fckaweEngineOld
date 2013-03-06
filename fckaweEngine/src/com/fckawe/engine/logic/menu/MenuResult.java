package com.fckawe.engine.logic.menu;

public abstract class MenuResult {
	
	public static short EXIT = 9999;
	
	protected short code;
	
	public MenuResult(final short code) {
		this.code = code;
	}
	
	public short getCode() {
		return code;
	}
	
	public boolean isExit() {
		return code == EXIT;
	}

}
