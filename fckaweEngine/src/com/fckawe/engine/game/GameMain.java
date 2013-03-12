package com.fckawe.engine.game;

import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.core.Heart;
import com.fckawe.engine.core.Session;
import com.fckawe.engine.game.menu.MainMenu;
import com.fckawe.engine.game.menu.MenuResult;
import com.fckawe.engine.game.menu.OptionsMenu;
import com.fckawe.engine.grafix.Bitmap;
import com.fckawe.engine.grafix.Bitmaps;
import com.fckawe.engine.grafix.BitmapsLogic;

public class GameMain implements Observer {
	
	protected UserInterface ui;
	protected GamePart currentGamePart;
	
	protected int saveMainMenuIdx;
	
	public GameMain() {
		ui = new UserInterface();
		ui.start();
	}
	
	public void start() {
		Heart heart = Session.getSession().getHeart();
		heart.addObserver(this);
		heart.addObserver(ui);
		runMainMenu();
		heart.requestExit();
	}
	
	public void exit() {
		if(currentGamePart != null) {
			currentGamePart.exit();
		}
		ui.exit();
	}
	
	protected void runMainMenu() {
		boolean exit = false;
		while(!exit) {
			MenuResult result = callMainMenu();
			switch(result.getCode()) {
			case MainMenu.START_GAME:
				// TODO: start game
				break;
			case MainMenu.CALL_OPTIONS_MENU:
				runOptionsMenu();
				break;
			case MainMenu.EXIT:
				exit = true;
				break;
			}
		}
	}

	protected MenuResult callMainMenu() {
		loadMenuGrafix();
		MainMenu mainMenu = createMainMenu();
		mainMenu.setActiveComponentIndex(saveMainMenuIdx);
		setCurrentGamePart(mainMenu);
		MenuResult result = mainMenu.run();
		saveMainMenuIdx = mainMenu.getActiveComponentIndex();
		return result;

	}
	
	protected void loadMenuGrafix() {
		BitmapsLogic bml = Session.getSession().getBitmapsLogic();
		Bitmaps bms = Session.getSession().getBitmaps();

		Bitmap bitmap;
		Bitmap[][] sprite;
		
		sprite = bml.cut("/menu/button.png", 290, 75);
		bms.putBitmapSprite(Bitmaps.MENU_BUTTON, sprite);
		bitmap = bml.load("/menu/background-main.png");
		bms.putBitmap(Bitmaps.MAIN_MENU_BACKGROUND, bitmap);
		bitmap = bml.load("/menu/background-options.png");
		bms.putBitmap(Bitmaps.OPTIONS_MENU_BACKGROUND, bitmap);
	}

	protected MainMenu createMainMenu() {
		return new MainMenu(ui.getScreen());
	}

	protected void runOptionsMenu() {
		boolean exit = false;
		while(!exit) {
			MenuResult result = callOptionsMenu();
			switch(result.getCode()) {
			case OptionsMenu.BACK:
				exit = true;
				break;
			}
		}
	}

	protected MenuResult callOptionsMenu() {
		OptionsMenu optionsMenu = createOptionsMenu();
		setCurrentGamePart(optionsMenu);
		MenuResult result = optionsMenu.run();
		return result;
	}

	protected OptionsMenu createOptionsMenu() {
		return new OptionsMenu(ui.getScreen());
	}

	@Override
	public void update(final Observable observable, final Object data) {
		if(observable == Session.getSession().getHeart()) {
			Heart.Event event = null;
			if (data instanceof Heart.Event) {
				event = (Heart.Event)data;
			} else if(data instanceof Heart.EventData) {
				Heart.EventData o = (Heart.EventData)data;
				event = o.getEvent();
			} else {
				throw new IllegalStateException("Unexpected data type: " + data.getClass().getName());
			}
			
			switch(event) {
			case TICK:
				if(currentGamePart != null) {
					ui.tick();
					currentGamePart.tick();
				}
				break;
			default:
				// do nothing
				break;
			}
		}
	}
	
	private void setCurrentGamePart(final GamePart gamePart) {
		currentGamePart = gamePart;
		if(ui != null) {
			ui.setCurrentGamePart(gamePart);
		}
	}

}
