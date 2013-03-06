package com.fckawe.engine.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.grafix.Fonts;
import com.fckawe.engine.grafix.Screen;
import com.fckawe.engine.logic.Heart;
import com.fckawe.engine.logic.Session;

public class UserInterface extends Canvas implements Observer {
	
	private static final long serialVersionUID = 8075843220995669488L;
	
	private MainWindow mainWindow;
	
	private final Screen screen;
	private int screenScale;
	
	private Dimension uiDimension;
	
	private double framesPerSecond;
	
	public UserInterface() {
		Dimension uiDimension = getUiDimension();
		setPreferredSize(uiDimension);
		setMinimumSize(uiDimension);
		setMaximumSize(uiDimension);
		setBackground(Color.BLACK);

		Dimension screenDimension = getScreenDimension(uiDimension);
		screen = new Screen(screenDimension);
	}
	
	public void start() {
		mainWindow = new MainWindow(this);
		mainWindow.setTitle("Application"); // TODO: Name
		mainWindow.setVisible(true);
	}
	
	private Dimension getUiDimension() {
		if (uiDimension == null) {
			int width = 800; // TODO: Configuration.DISPLAY_WIDTH;
			int height = 600; // TODO: Configuration.DISPLAY_HEIGHT;
			uiDimension = new Dimension(width, height);
		}
		return uiDimension;
	}
	
	private Dimension getScreenDimension(final Dimension uiDimension) {
		screenScale = 1; // TODO: Configuration.SCREEN_SCALE;
		if (screenScale == 1) {
			return uiDimension;
		}
		double width = uiDimension.getWidth() / screenScale;
		double height = uiDimension.getHeight() / screenScale;
		Dimension screenDimension = new Dimension((int) width, (int) height);
		return screenDimension;
	}

	@Override
	public void update(final Observable observable, final Object data) {
		if(observable == Session.getSession().getHeart()) {
			Heart.Event event = null;
			Object eventData = null;
			if (data instanceof Heart.Event) {
				event = (Heart.Event)data;
			} else if(data instanceof Heart.EventData) {
				Heart.EventData o = (Heart.EventData)data;
				event = o.getEvent();
				eventData = o.getData();
			} else {
				throw new IllegalStateException("Unexpected data type: " + data.getClass().getName());
			}
			
			switch(event) {
			case HEART_START:
				setFocusTraversalKeysEnabled(false);
				requestFocus();
				break;
			case RENDER:
				render();
				break;
			case SHOW:
				showRenderedImage();
				break;
			case FPS_UPDATED:
				framesPerSecond = (Double) eventData;
				break;
			default:
				// do nothing
				break;
			}
		}
	}
	
	protected void render() {
		screen.clear(0);
		
		// TODO: render
		
		Session.getSession().getFontsLogic().draw(screen, "SMALLBLUE", "FPS:" + (int) framesPerSecond, 10, 10);
		
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
		} else {
			Graphics g = bs.getDrawGraphics();
			render(g);
		}
	}
	
	private synchronized void render(final Graphics g) {
		int width = (int) uiDimension.getWidth();
		int height = (int) uiDimension.getHeight();

		g.translate((getWidth() - (width * screenScale)) / 2,
				(getHeight() - (height * screenScale)) / 2);
		g.clipRect(0, 0, width, height);

		g.drawImage(screen.getImage(), 0, 0, width, height, null);
	}
	
	private void showRenderedImage() {
		BufferStrategy bs = getBufferStrategy();
		if (bs != null) {
			bs.show();
		}
	}

}
