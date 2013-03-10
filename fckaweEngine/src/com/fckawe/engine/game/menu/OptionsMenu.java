package com.fckawe.engine.game.menu;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.menu.elements.Button;
import com.fckawe.engine.game.menu.elements.MenuComponent;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.grafix.Screen;


public class OptionsMenu extends Menu<MenuResult> {
	
	public static final int BACK = 1;

	public OptionsMenu(final Screen screen) {
		super(screen);
	}

	@Override
	public void addComponents() {
		String buttonsFont = getButtonsFontName();
		
		MenuComponent e;
		e = createBackButton(buttonsFont);
		addElement(e);
	}

	protected MenuComponent createBackButton(final String fontName) {
		String label = Session.getSession().getLang().getString("MENU_BACK");
		return createButton(BACK, label, fontName);
	}

	@Override
	protected MenuResult createResult(final Button button) {
		return new MenuResult(button.getActionId());
	}

	@Override
	public Bitmap getBackgroundBitmap() {
		return Session.getSession().getBitmaps().getBitmap(Bitmaps.OPTIONS_MENU_BACKGROUND);
	}

}
