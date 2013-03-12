package com.fckawe.engine.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.core.Configuration;
import com.fckawe.engine.core.Heart;
import com.fckawe.engine.core.Session;
import com.fckawe.engine.grafix.Screen;
import com.fckawe.engine.input.InputHandler;

public class UserInterface extends Canvas implements Observer {
	
	private static final long serialVersionUID = 8075843220995669488L;
	
	protected GamePart currentGamePart;
	
	private MainWindow mainWindow;
	private final Screen screen;
	private int screenScale;
	private Dimension uiDimension;
	private double framesPerSecond;
	
	private InputHandler inputHandler;
	
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
		Session session = Session.getSession();
		Configuration cfg = session.getConfiguration();
		mainWindow.setTitle(cfg.getGameName() + " [powered by " + session.getEngineName() + "]");
		inputHandler = new InputHandler();
		addKeyListener(inputHandler);
		mainWindow.setVisible(true);
	}
	
	public void exit() {
		mainWindow.setVisible(false);
		mainWindow.dispose();
		Session.getSession().getMainLogger().info("User interface stopped.");
	}
	
	private Dimension getUiDimension() {
		if (uiDimension == null) {
			Configuration cfg = Session.getSession().getConfiguration();
			int width = cfg.getDisplayWidth();
			int height = cfg.getDisplayHeight();
			uiDimension = new Dimension(width, height);
		}
		return uiDimension;
	}
	
	private Dimension getScreenDimension(final Dimension uiDimension) {
		Configuration cfg = Session.getSession().getConfiguration();
		screenScale = cfg.getScreenScale();
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
		
		if(currentGamePart != null) {
			currentGamePart.render(screen);
		}
		
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
	
	public void setCurrentGamePart(final GamePart currentGamePart) {
		this.currentGamePart = currentGamePart;
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public void tick() {
		inputHandler.tick();
	}

}
