package computervision;

import java.awt.image.BufferedImage;

public interface ClassificationStrategy {

	ImageLabel scanImage(BufferedImage imgInput);
}
