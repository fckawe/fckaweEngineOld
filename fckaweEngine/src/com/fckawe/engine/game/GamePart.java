package com.fckawe.engine.game;

import java.awt.event.KeyListener;

import com.fckawe.engine.grafix.Screen;

public interface GamePart extends KeyListener {
	
	public void tick();
	
	public void render(Screen screen);
	
	public void exit();

}
