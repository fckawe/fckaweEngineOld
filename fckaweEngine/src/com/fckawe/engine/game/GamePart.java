package com.fckawe.engine.game;

import com.fckawe.engine.grafix.Screen;

public interface GamePart {
	
	public void tick();
	
	public void render(Screen screen);
	
	public void exit();

}
