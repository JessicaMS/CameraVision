package visionPatterns;

import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

import computervision.ImageLabel;

public class Classifier implements CameraProcessor {

	private Semaphore mutex = new Semaphore(1);
	private String result;
	private BufferedImage lastImage;

	public Classifier() {

		result = "None";
	}


	public synchronized void updateBufferedImage(BufferedImage newImage) {
		if (mutex.tryAcquire()) {
			lastImage = newImage;
			mutex.release();
		}
	}


	@Override
	public ImageLabel scanImage() {
		long start, elapsed;

		try {
			mutex.acquire();
			String newResult = null;
			start = System.currentTimeMillis();

			if (!newResult.equals(result)) {
				result = newResult;
			}


			elapsed = System.currentTimeMillis()-start;
			System.out.println("Scan took " + elapsed + "milliseconds\n");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			mutex.release();
		}

		return null;
	}
}
