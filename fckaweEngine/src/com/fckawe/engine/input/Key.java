package com.fckawe.engine.input;

public enum Key {
	
	UP, DOWN, LEFT, RIGHT, ENTER;

	private final State state;
	private final State nextState;
	
	private Key() {
		state = new State();
		nextState = new State();
	}
	
	public void setNextState(final boolean pressed, final boolean withShift, final boolean withAlt) {
		nextState.pressed = pressed;
		nextState.withShift = withShift;
		nextState.withAlt = withAlt;
	}
	
	public void tick() {
		nextState.copyTo(state);
	}
	
	public void consume() {
		state.reset();
		nextState.reset();
	}
	
	public boolean isPressed() {
		return state.pressed;
	}
	
	public boolean isPressedWithShift() {
		return state.pressed && state.withShift && !state.withAlt;
	}
	
	public boolean isPressedWithAlt() {
		return state.pressed && state.withAlt && !state.withShift;
	}
	
	public boolean isPressedWithShiftAndAlt() {
		return state.pressed && state.withShift && state.withAlt;
	}
	
	private class State {
		
		private boolean pressed;
		private boolean withShift;
		private boolean withAlt;
		
		private void copyTo(final State other) {
			other.pressed = this.pressed;
			other.withShift = this.withShift;
			other.withAlt = this.withAlt;
		}
		
		private void reset() {
			pressed = false;
			withShift = false;
			withAlt = false;
		}
		
	}

}
