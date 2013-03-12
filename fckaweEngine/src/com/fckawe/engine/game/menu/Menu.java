package com.fckawe.engine.game.menu;

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
import com.fckawe.engine.input.Key;

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
		int maxCompIdx = components.size() - 1;
		if(Key.UP.isPressed()) {
			Key.UP.consume();
			activeCompIdx--;
			if(activeCompIdx < 0) {
				activeCompIdx = maxCompIdx;
			}
		} else if(Key.DOWN.isPressed()) {
			Key.DOWN.consume();
			activeCompIdx++;
			if(activeCompIdx > maxCompIdx) {
				activeCompIdx = 0;
			}
		} else if(Key.ENTER.isPressed()) {
			Key.ENTER.consume();
			MenuComponent activeComponent = components.get(activeCompIdx);
			if(activeComponent instanceof Button) {
				((Button)activeComponent).performClick();
			}
		}
		
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
