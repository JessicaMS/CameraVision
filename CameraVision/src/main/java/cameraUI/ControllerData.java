package cameraUI;

import java.util.Observable;

public class ControllerData extends Observable {
	private String locationName;
	
	ControllerData() {
		locationName = "(None)";
	}
	
	private void locationNameChanged() {
		setChanged(); 
		notifyObservers();
	}
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
		
		locationNameChanged();
	}
	
	public String getLocationName() {
		return this.locationName;
	}
}
