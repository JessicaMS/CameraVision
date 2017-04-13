package computervision;

import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Semaphore;

import cameras.capturedImage;

public class Classifier implements CameraObserver {
	private ClassificationStrategy myImgProcessor;
	private ImageLabel resultingLabel;

	private Semaphore mutex = new Semaphore(1);
	private String result;
	private BufferedImage lastImage;
	private volatile boolean requestNew;

	public Classifier(ClassificationStrategy myImgProcessor, capturedImage observable) {
		resultingLabel = new ImageLabel();
		observable.addObserver(this);

		this.myImgProcessor = myImgProcessor;

		requestNew = false;
		lastImage = null;
	}

	public synchronized void updateBufferedImage(BufferedImage newImage) {
		if (requestNew) {
			if (mutex.tryAcquire()) {
				System.out.println("New image recvd");
				lastImage = newImage;
				mutex.release();
				requestNew = false;
			}
		}

	}

	public ImageLabel processImage() {
		long start, elapsed;
		requestNew = true;
		while(requestNew == true) { 
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		try {
			mutex.acquire();
			start = System.currentTimeMillis();

			resultingLabel.setLabel(this.myImgProcessor.scanImage(lastImage));

			elapsed = System.currentTimeMillis()-start;
			System.out.println("Scan took " + elapsed + "milliseconds\n");
		} catch (InterruptedException e1) {
			resultingLabel.setLabelName("(None)");
			resultingLabel.setProbability(100.0);
			e1.printStackTrace();
		} finally {
			mutex.release();
		}

		return resultingLabel;
	}


	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof capturedImage) {

			updateBufferedImage(((capturedImage)o).getCameraCapture());
		}
	}

}
