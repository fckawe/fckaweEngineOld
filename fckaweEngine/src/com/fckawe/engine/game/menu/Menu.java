package com.fckawe.engine.game.menu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.GamePart;
import com.fckawe.engine.game.menu.elements.Button;
import com.fckawe.engine.game.menu.elements.ButtonListener;
import com.fckawe.engine.game.menu.elements.MenuComponent;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Screen;

public abstract class Menu<R extends MenuResult> implements GamePart, ButtonListener {
	
	private final List<MenuComponent> components = new ArrayList<MenuComponent>();
	
	private Screen screen;
	private int activeCompIdx;
	private BlockingQueue<R> q;
	
	public Menu(final Screen screen) {
		this.screen = screen;
	}
	
	public abstract void addComponents();
	
	public abstract Bitmap getBackgroundBitmap();
	
	public R run() {
		q = new ArrayBlockingQueue<R>(1);
		
		removeAllComponents();
		addComponents();
		placeComponents();
		
		R result = null;
		while(result == null) {
			try {
				result = q.poll(1, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Session.getSession().getMainLogger().warn("Queue poll got aborted!", e);
			}
		}
		
		return result;
	}
	
	protected Button createButton(final int actionId, final String label,
			final String fontName) {
		Button button = new Button(actionId, label, fontName);
		button.addButtonListener(this);
		return button;
	}
	
	protected void removeAllComponents() {
		components.clear();
	}
	
	protected void addElement(final MenuComponent element) {
		components.add(element);
	}
	
	protected void addElement(final int index, final MenuComponent element) {
		components.add(index, element);
	}
	
	protected void placeComponents() {
		int posY = getStartPosY();
		int posX = getPosX();
		int offset = getOffsetBetweenComponents();
		
		for(MenuComponent component : components) {
			component.setPosX(posX);
			component.setPosY(posY);
			posY += offset;
		}
	}
	
	private int getWidestComponentWidth() {
		int width = 0;
		for(MenuComponent component : components) {
			if(component.getWidth() > width) {
				width = component.getWidth();
			}
		}
		return width;
	}
	
	protected int getPosX() {
		int width = screen.getWidth();
		int componentsWidth = getWidestComponentWidth();
		return (width - componentsWidth) / 2;
	}
	
	protected int getStartPosY() {
		int offset = getOffsetBetweenComponents();
		int height = screen.getHeight();
		boolean first = true;
		for(MenuComponent component : components) {
			height -= component.getHeight();
			if(first) {
				height -= offset;
				first = false;
			}
		}
		return height / 2;
	}
	
	protected int getOffsetBetweenComponents() {
		return 75;
	}
	
	@Override
	public void tick() {
		for(MenuComponent component : components) {
			component.tick();
		}
	}
	
	@Override
	public void render(final Screen screen) {
		Bitmap background = getBackgroundBitmap();
		if(background != null) {
			screen.blit(background, 0, 0);
		}
		
		int idx = 0;
		for(MenuComponent component : components) {
			if(idx == activeCompIdx) {
				component.renderActive(screen);
			} else {
				component.render(screen);
			}
			idx++;
		}
	}
	
	protected String getButtonsFontName() {
		return "BIGRED";
	}

	@Override
	public void keyPressed(final KeyEvent event) {
		int keyCode = event.getKeyCode();
		int keyCodeNew;
		
		switch(keyCode) {
		case KeyEvent.VK_ENTER:
			// TODO: bei Text inputs nach unten springen...
			keyCodeNew = keyCode;
			break;
		default:
			keyCodeNew = keyCode;
		}
		
		int maxCompIdx = components.size() - 1;
		
		switch(keyCodeNew) {
		case KeyEvent.VK_UP:
			activeCompIdx--;
			if(activeCompIdx < 0) {
				activeCompIdx = maxCompIdx;
			}
			break;
		case KeyEvent.VK_DOWN:
			activeCompIdx++;
			if(activeCompIdx > maxCompIdx) {
				activeCompIdx = 0;
			}
			break;
		case KeyEvent.VK_ENTER:
			event.consume();
			MenuComponent activeComponent = components.get(activeCompIdx);
			if(activeComponent instanceof Button) {
				((Button)activeComponent).performClick();
			}
			break;
		}
	}
	
	@Override
	public void keyTyped(final KeyEvent event) {
		// nothing to do
	}

	@Override
	public void keyReleased(final KeyEvent event) {
		// nothing to do
	}
	
	public void buttonClicked(final Button button) {
		R result = createResult(button);
		q.offer(result);
	}
	
	protected abstract R createResult(final Button button);
	
	public void setActiveComponentIndex(final int activeCompIdx) {
		this.activeCompIdx = activeCompIdx;
	}
	
	public int getActiveComponentIndex() {
		return activeCompIdx;
	}
	
	public void exit() {
		// nothing to do
	}

}
