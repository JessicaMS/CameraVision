package cameras;

import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public interface Camera {
	public void initialize();
	
	public void close();
	
	public JPanel getPanel();
	
	public BufferedImage getImage();
	
	public void registerCameraObserver(CameraObserver myObserver);
	
	public void registerFPSObserver(FPSObserver myObserver);
}
