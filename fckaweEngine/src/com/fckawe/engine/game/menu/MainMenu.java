package com.fckawe.engine.game.menu;

import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.menu.elements.Button;
import com.fckawe.engine.game.menu.elements.MenuComponent;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.grafix.Screen;

public class MainMenu extends Menu<MenuResult> {

	public static final int START_GAME = 1;
	public static final int CALL_OPTIONS_MENU = 2;
	public static final int EXIT = 3;

	public MainMenu(final Screen screen) {
		super(screen);
	}
	
	@Override
	public void addComponents() {
		String buttonsFont = getButtonsFontName();
		
		MenuComponent e;
		e = createStartGameButton(buttonsFont);
		addElement(e);
		e = createCallOptionsMenuButton(buttonsFont);
		addElement(e);
		e = createExitButton(buttonsFont);
		addElement(e);
	}
	
	protected MenuComponent createStartGameButton(final String fontName) {
		String label = Session.getSession().getLang().getString("MAIN_MENU_START");
		return createButton(START_GAME, label, fontName);
	}
	
	protected MenuComponent createCallOptionsMenuButton(final String fontName) {
		String label = Session.getSession().getLang().getString("MAIN_MENU_OPTIONS");
		return createButton(CALL_OPTIONS_MENU, label, fontName);
	}
	
	protected MenuComponent createExitButton(final String fontName) {
		String label = Session.getSession().getLang().getString("MAIN_MENU_EXIT");
		return createButton(EXIT, label, fontName);
	}

	@Override
	protected MenuResult createResult(final Button button) {
		return new MenuResult(button.getActionId());
	}

	@Override
	public Bitmap getBackgroundBitmap() {
		return Session.getSession().getBitmaps().getBitmap(Bitmaps.MAIN_MENU_BACKGROUND);
	}

}
