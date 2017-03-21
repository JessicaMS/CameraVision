package computervision;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import cameras.CameraObserver;

public class QRReader implements ImageProcessor {

	private BufferedImage readNext;
	private volatile boolean readYet;
	private volatile boolean imageProcessing;
	private volatile boolean threadRunning;
	
	private String result;
	
	
	public QRReader() {
		readNext = null;
		readYet = true;
		imageProcessing = false;
		threadRunning = true;
		result = "None";
	}
	
	private synchronized boolean isReadYet() {
		return readYet;
	}

	private synchronized void setReadYet(boolean readYet) {
		this.readYet = readYet;
	}

	public synchronized void updateBufferedImage(BufferedImage newImage) {
		if(isReadYet()) {
			System.out.println("New image recvd");
			readNext = newImage;
			setReadYet(false);
		}
	}

	public boolean isImageProcessing() {
		return imageProcessing;
	}

	public void setImageProcessing(boolean runClassifier) {
		this.imageProcessing = runClassifier;
	}
	
	public void stopThread() {
		threadRunning = false;
	}
	
	private void resultsChanged() {
//		notifyObservers();
		System.out.println(result);
		result = "(Cleared)";
		
	}
	
//	private void notifyObservers() {
//		for(ClassificationsObserver singleObserver: myObservers) {
//			singleObserver.updatePredictions(results);
//		}
//	}


	@Override
	public void run() {
		long start, elapsed;

		while(threadRunning) {
			if (imageProcessing == true && !isReadYet()) {
				start = System.currentTimeMillis();
				LuminanceSource source = new BufferedImageLuminanceSource(readNext);
				BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

				try {
					result = new MultiFormatReader().decode(bitmap).getText();
					resultsChanged();
				} catch (NotFoundException e) {
					// fall thru, it means there is no QR code in image
				}
				
				setReadYet(true);
				elapsed = System.currentTimeMillis()-start;
				System.out.println("QR Scan took " + elapsed + "milliseconds\n");
				

			}
		}


		
	}
}
