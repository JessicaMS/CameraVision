package cameras;

import java.awt.image.BufferedImage;
import java.util.Observable;

public class capturedImage extends Observable {
	private BufferedImage cameraCapture;
	
	capturedImage() {
		cameraCapture = null;
	}

	public void captureChanged()
	{
		setChanged(); 
		notifyObservers();
	}
	

	public void setCameraCapture(BufferedImage cameraCapture) {
		//System.out.println("new set");
		this.cameraCapture = cameraCapture;
		captureChanged();
		
	}
	
	public BufferedImage getCameraCapture() {
		return cameraCapture;
	}
}
