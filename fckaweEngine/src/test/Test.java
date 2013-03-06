package test;

import com.fckawe.engine.logic.MainApplication;
import com.fckawe.engine.logic.Session;
import com.fckawe.engine.ui.UserInterface;

public class Test {
	
	public static void main(String[] args) {
		UserInterface ui = new UserInterface();
		ui.start();
		MainApplication app = new MainApplication();
		Session.getSession().getHeart().addObserver(ui);
		app.run();
	}
	
}
