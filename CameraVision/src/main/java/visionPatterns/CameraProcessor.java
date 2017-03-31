package visionPatterns;

import cameras.CameraObserver;
import computervision.ImageLabel;

public interface CameraProcessor extends CameraObserver {
	
	ImageLabel scanImage();
	
	
}
