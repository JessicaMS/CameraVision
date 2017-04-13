package computervision;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;
import java.util.Vector;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import computervision.ImageLabel;

public class QRReader implements ClassificationStrategy  {
	protected Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

	private MultiFormatReader reader;
	private double scanAreaPercent = 0.7;

	private String result;

	public QRReader() {
		result = "None";
		reader = new MultiFormatReader();
		Vector<BarcodeFormat> formats = new Vector<BarcodeFormat>();
		
		formats.add(BarcodeFormat.QR_CODE);
		 
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, true);
        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);

        reader.setHints(hints);
	}

	@Override
	public ImageLabel scanImage(BufferedImage imgInput) {
		LuminanceSource source = new BufferedImageLuminanceSource(imgInput);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		try {
			result = reader.decodeWithState(bitmap).getText();
		} catch (NotFoundException e) {
			// fall thru, it means there is no QR code in image
			result = "(None)";
		} finally {
			reader.reset();
		}
		return new ImageLabel(result, 100.0);
	}
}
