package computervision;

import java.awt.image.BufferedImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import computervision.ImageLabel;

public class QRReader implements ImageProcessor  {

	private String result;

	public QRReader() {
		result = "None";
	}

	@Override
	public ImageLabel scanImage(BufferedImage imgInput) {
		LuminanceSource source = new BufferedImageLuminanceSource(imgInput);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		try {
			result = new MultiFormatReader().decode(bitmap).getText();
		} catch (NotFoundException e) {
			// fall thru, it means there is no QR code in image
			result = "(None)";
		}
		return new ImageLabel(result, 100.0);
	}
}
