package com.fckawe.engine.logic;

import java.util.Observable;
import java.util.Observer;

import com.fckawe.engine.logic.menu.MainMenu;
import com.fckawe.engine.logic.menu.MainMenuResult;
import com.fckawe.engine.logic.menu.OptionsMenu;
import com.fckawe.engine.logic.menu.OptionsMenuResult;

public class MainApplication implements Observer, Heart.ExitListener {
	
	private Heart heart;
	
	public MainApplication() {
		heart = createHeart();
		createSession(heart);
		heart.addObserver(this);
	}
	
	public void run() {
		startHeart(heart);
		runMainMenu();
	}
	
	protected Heart createHeart() {
		return new Heart(this);
	}
	
	protected void createSession(final Heart heart) {
		Session.init(heart);
	}
	
	protected void startHeart(final Heart heart) {
		Thread thread = new Thread(heart);
		thread.setName("Heart#" + Session.getSession().getEngineName());
		thread.start();
	}
	
	protected void runMainMenu() {
		boolean exit = false;
		while(!exit) {
			MainMenuResult result = callMainMenu();
			if(result.isExit()) {
				exit = true;
			} else {
				switch(result.getCode()) {
				case MainMenuResult.START_GAME:
					// TODO: start game
					break;
				case MainMenuResult.CALL_OPTIONS_MENU:
					runOptionsMenu();
					break;
				}
			}
		}
	}
	
	protected MainMenuResult callMainMenu() {
		MainMenu mainMenu = createMainMenu();
		MainMenuResult result = mainMenu.run();
		return result;
		
	}
	
	protected MainMenu createMainMenu() {
		return new MainMenu();
	}
	
	protected void runOptionsMenu() {
		boolean exit = false;
		while(!exit) {
			OptionsMenuResult result = callOptionsMenu();
			if(result.isExit()) {
				exit = true;
			} else {
				switch(result.getCode()) {
				// TODO: implement options menu
				}
			}
		}
	}
	
	protected OptionsMenuResult callOptionsMenu() {
		OptionsMenu optionsMenu = createOptionsMenu();
		OptionsMenuResult result = optionsMenu.run();
		return result;
	}
	
	protected OptionsMenu createOptionsMenu() {
		return new OptionsMenu();
	}

	@Override
	public void exit() {
		// TODO: exit
		System.out.println("exit");
		System.exit(0);
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
				tick();
				break;
			default:
				// do nothing
				break;
			}
		}
	}
	
	protected void tick() {
		// TODO: tick
	}

}
