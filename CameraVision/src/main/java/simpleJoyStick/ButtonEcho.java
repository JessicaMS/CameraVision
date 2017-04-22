package simpleJoyStick;

import java.util.Observable;
import java.util.Observer;

public class ButtonEcho implements Observer {

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof ButtonData) {
			ButtonData btn = ((ButtonData)o);
			int index = (int)arg;
			
			System.out.println(btn.getButtonName(index) + " : " + btn.getButtonValue(index) );
		}
		
	}
	
	

}
