package com.fckawe.engine.core;

import com.fckawe.engine.game.GameMain;

public class MainApplication implements Heart.ExitListener {
	
	private Heart heart;
	
	protected GameMain game;
	
	public MainApplication() {
		heart = createHeart();
		createSession(heart);
	}
	
	public void run() {
		startHeart(heart);
		game = new GameMain();
		game.start();
	}
	
	protected Heart createHeart() {
		return new Heart(this);
	}
	
	protected void createSession(final Heart heart) {
		Session.newSession(heart);
	}
	
	protected void startHeart(final Heart heart) {
		Thread thread = new Thread(heart);
		thread.setName("Heart#" + Session.getSession().getEngineName());
		thread.start();
	}
	
	@Override
	public void exit() {
		game.exit();
	}

}
