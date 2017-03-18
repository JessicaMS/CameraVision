package cameras;

import java.awt.image.BufferedImage;

public interface CameraObserver {

	void updateBufferedImage(BufferedImage newImage);
}
