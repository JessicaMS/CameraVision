package cameras;

import javax.swing.JPanel;

public interface Camera {
	public void initialize();
	
	public void close();
	
	public JPanel getPanel();
	
	public capturedImage getCapturedImage();
}
