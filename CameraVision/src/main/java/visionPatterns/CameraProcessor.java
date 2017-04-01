package visionPatterns;

import java.util.Observer;

import computervision.ImageLabel;

public interface CameraProcessor extends Observer {
	
	ImageLabel scanImage();
	
	
}
