package com.fckawe.engine.game.menu.elements;

import java.util.ArrayList;
import java.util.List;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.grafix.Screen;

public class Button extends MenuComponent {
	
	private static final Bitmap[][] SPRITE =
			Session.getSession().getBitmaps().getBitmapSprite(Bitmaps.MENU_BUTTON);
	private static final Bitmap BITMAP_INACTIVE = SPRITE[0][0];
	private static final Bitmap BITMAP_ACTIVE = SPRITE[0][1];
	
	private final int actionId;
	private final String label;
	private final String fontName;
	
	private List<ButtonListener> listeners;

	public Button(final int actionId, final String label, final String fontName) {
		super(BITMAP_INACTIVE.getDimension());
		this.actionId = actionId;
		this.label = label;
		this.fontName = fontName;
	}
	
	public void addButtonListener(final ButtonListener listener) {
		if(listeners == null) {
			listeners = new ArrayList<ButtonListener>();
		}
		listeners.add(listener);
	}

	@Override
	public void render(final Screen screen) {
		boolean active = false; // TODO: clicked?!
		renderInternal(screen, active);
	}

	@Override
	public void renderActive(final Screen screen) {
		renderInternal(screen, true);
	}
	
	private void renderInternal(final Screen screen, final boolean active) {
		if(active) {
			screen.blit(BITMAP_ACTIVE, getPosX(), getPosY());
		} else {
			screen.blit(BITMAP_INACTIVE, getPosX(), getPosY());
		}
		Session.getSession().getFontsLogic().drawCentered(screen, fontName, label,
				getPosX() + getWidth() / 2, getPosY() + getHeight() / 2);
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
	}
	
	public int getActionId() {
		return actionId;
	}
	
	public void performClick() {
		if(listeners != null) {
			for(ButtonListener listener : listeners) {
				listener.buttonClicked(this);
			}
		}
	}

}