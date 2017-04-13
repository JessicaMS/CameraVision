package computervision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.concurrent.Semaphore;

import javax.imageio.ImageIO;

import cameras.capturedImage;

public class Recorder implements CameraObserver {
	private Semaphore mutex = new Semaphore(1);
	private volatile boolean requestNew;
	private BufferedImage lastImage;
	
	
	public Recorder(capturedImage observable) {
		observable.addObserver(this);

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
	
	private void saveImage(BufferedImage lastImage) {
		String fileName = String.valueOf(System.currentTimeMillis());
		File outputfile = new File("./captures/" + fileName + ".jpg");
	    try {
			ImageIO.write(lastImage, "jpg", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void processImage() {
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

			saveImage(this.lastImage);
			
			elapsed = System.currentTimeMillis()-start;
			System.out.println("Scan took " + elapsed + "milliseconds\n");
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			mutex.release();
		}

	}





	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof capturedImage) {

			updateBufferedImage(((capturedImage)o).getCameraCapture());
		}
	}

}
