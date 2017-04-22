package simpleJoyStick;

import java.util.Observable;

//types:  0 = button

class Input {
	public String name;
	public float value;
	public int type;
	
	Input() {
		this.value = (float) 0.0;
	}
	
}

public class ButtonData extends Observable {
	private Input joyStick[];
	
	public ButtonData(int componentCount) {
		joyStick = new Input[componentCount];
		for(int i = 0 ; i < componentCount; i++) {
			joyStick[i] = new Input();
		}
	}
	
	private void buttonChanged(int index) {
		this.setChanged();
		this.notifyObservers(index);
	}
	
	public float getButtonValue(int index) {
		return joyStick[index].value;
	}
	
	public String getButtonName(int index) {
		return joyStick[index].name;
	}


	public void setButton(int index, String buttonName, float buttonValue) {
		if(joyStick[index].value != buttonValue) {
			joyStick[index].name = buttonName;
			joyStick[index].value = buttonValue;
			joyStick[index].type = 0;
			
			buttonChanged(index);
		}

	}
	
	public void setHatPosition(int index, float buttonValue) {
		if(joyStick[index].value != buttonValue) {
			joyStick[index].name = "Hat";
			joyStick[index].value = buttonValue;
			joyStick[index].type = 1;
			
			buttonChanged(index);
		}
	}
	
	public void setAxisPosition(int index, String axisName, float axisValue) {
		if(joyStick[index].value != axisValue) {
			joyStick[index].name =  axisName;
			joyStick[index].value = axisValue;
			joyStick[index].type = 2;
			
			buttonChanged(index);
		}
	}
}
