package com.fckawe.engine.game.menu.elements;

import java.awt.Dimension;
import java.awt.Point;

import com.fckawe.engine.grafix.Screen;

public abstract class MenuComponent {

	private int posX;
	private int posY;
	private final int width;
	private final int height;

	public MenuComponent(final Dimension dim) {
		this.width = (int) dim.getWidth();
		this.height = (int) dim.getHeight();
	}
	
	public void setPos(final Point pos) {
		this.posX = (int) pos.getX();
		this.posY = (int) pos.getY();
	}
	
	public void setPosX(final int posX) {
		this.posX = posX;
	}
	
	public int getPosX() {
		return posX;
	}
	
	public void setPosY(final int posY) {
		this.posY = posY;
	}

	public int getPosY() {
		return posY;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public abstract void render(Screen screen);

	public abstract void renderActive(Screen screen);

	public abstract void tick();
	
}
