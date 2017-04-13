package computervision;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import java.awt.image.DataBufferInt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import org.nd4j.linalg.api.ndarray.*;
import org.nd4j.linalg.factory.Nd4j;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

public class Image2Tensor {

	private static int[] toByteArrayFromIntImage(BufferedImage bufferedImage) {
		int[] colors =  ((DataBufferInt)bufferedImage.getData().getDataBuffer()).getData();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		int[] byChannel = new int[width*height*3];
		for(int i = 0; i < colors.length; i++) {
			byChannel[i] = 					(colors[i] >> 16 & 0xFF);
			byChannel[i+width*height] = 	(colors[i] >> 8 & 0xFF);
			byChannel[i+width*height*2] = 	(colors[i] & 0xFF);
		}
		
		return byChannel;
	}

	private static int[] toByteArrayFromByteImage(BufferedImage bufferedImage) {
		byte[] colorsB = ((DataBufferByte)bufferedImage.getData().getDataBuffer()).getData();
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();

		int[] byChannel = new int[width*height*3];
		for(int i = 0; i < colorsB.length; i+=3) {
			byChannel[i/3] = 					colorsB[i] & 0xFF;
			byChannel[i/3+width+height] = 		colorsB[i+1] & 0xFF;
			byChannel[i/3+width*height*2] = 	colorsB[i+2] & 0xFF;
		}

		return byChannel;
	}

	private static int[] splitColorChannels(BufferedImage bufferedImage) {		
		if (bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB ||
				bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB_PRE ||
				bufferedImage.getType() == BufferedImage.TYPE_INT_BGR ||
				bufferedImage.getType() == BufferedImage.TYPE_INT_RGB) {
			return toByteArrayFromIntImage(bufferedImage);
		} else {
			return toByteArrayFromByteImage(bufferedImage);
		}
	}
	
	public static INDArray image2Tensor(BufferedImage bufferedImage, int height, int width, int channels) {
		BufferedImage imgResized = null;
		
		if(bufferedImage.getHeight() != height || bufferedImage.getWidth() != width) {
			System.out.println("Automatically scaling image...");
			imgResized = Scalr.resize(bufferedImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, height, width);	
		} else {
			System.out.println("Warning:  Already resized");
			imgResized = bufferedImage;
		}

		int[] rawData = splitColorChannels(imgResized);
		float[] rawAsFloat = new float[rawData.length];

		for(int i = 0; i < rawData.length; i++) {
			rawAsFloat[i] = (float)rawData[i];
		}

		INDArray tensor = Nd4j.create(rawAsFloat, new int[]{1, 3, 224, 224});
		
		return tensor;
	}

	public static void main(String[] args) {
//		BufferedImage imgInput = null;
//
//		try {
//			imgInput = ImageIO.read(new File("C:/captures/cat2_sm.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//INDArray tensor = image2Tensor(imgInput, 3, 224, 224);
		
//		System.out.println("Tensor size: " + tensor.shapeInfoToString());
//		System.out.println(tensor);

	}


}
