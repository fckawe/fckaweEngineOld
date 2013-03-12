package com.fckawe.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

public class InputHandler implements KeyListener {
	
	private final Map<Integer, Key> keyMap = new HashMap<Integer, Key>();
	
	public InputHandler() {
		initKeyMap();
	}
	
	protected void initKeyMap() {
		keyMap.clear();
		keyMap.put(KeyEvent.VK_UP, Key.UP);
		keyMap.put(KeyEvent.VK_DOWN, Key.DOWN);
		keyMap.put(KeyEvent.VK_LEFT, Key.LEFT);
		keyMap.put(KeyEvent.VK_RIGHT, Key.RIGHT);
		keyMap.put(KeyEvent.VK_ENTER, Key.ENTER);
	}

	public void tick() {
		for(Key key : keyMap.values()) {
			key.tick();
		}
	}
	
	@Override
	public void keyPressed(final KeyEvent event) {
		toggle(event, true);
	}
	
	@Override
	public void keyReleased(final KeyEvent event) {
		toggle(event, false);
	}
	
	@Override
	public void keyTyped(final KeyEvent event) {
		// nothing to do
	}
	
	private void toggle(final KeyEvent event, final boolean pressed) {
		Key key = keyMap.get(event.getKeyCode());
		if(key != null) {
			key.setNextState(pressed, event.isShiftDown(), event.isAltDown());
		}
	}

}
