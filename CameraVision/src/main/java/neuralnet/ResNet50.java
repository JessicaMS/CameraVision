package neuralnet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.TrainedModels;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.Utils.ImageNetLabels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;

public class ResNet50 {
	
	private static final String modelJsonFilepath = "C:/captures/models/";
	private static final String weightsHdf5Filepath = "C:/captures/hdf5/";
	private ComputationGraph graphNet;
	
	public ArrayList<Classification> decodePredictions(INDArray predictions) {
		ArrayList<String> labels;
		ArrayList<Classification> results = new ArrayList<Classification>();
		
		int[] top5 = new int[5];
		float[] top5Prob = new float[5];

		labels = ImageNetLabels.getLabels();

		//brute force collect top 5
		int i = 0;
		for (int batch = 0; batch < predictions.size(0); batch++) {
			INDArray currentBatch = predictions.getRow(batch).dup();
			while (i < 5) {
				top5[i] = Nd4j.argMax(currentBatch, 1).getInt(0, 0);
				top5Prob[i] = currentBatch.getFloat(batch, top5[i]);
				currentBatch.putScalar(0, top5[i], 0);
				results.add(new Classification( labels.get(top5[i]),  top5Prob[i] * 100 ) );
				i++;
			}
		}
		return results;
	}

	public void loadResNet50() {
		//		int rngSeed = 1337; // random number seed for reproducibility
		//		int outputNum = 1000; // number of output classes

		String modelJsonFilename = modelJsonFilepath + "resnet_try.json";
		String weightsHdf5Filename = weightsHdf5Filepath + "resnet50_weights_tf_dim_ordering_tf_kernels.h5";

		try {
			graphNet = KerasModelImport.importKerasModelAndWeights(modelJsonFilename, weightsHdf5Filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		graphNet.init();
		System.out.println("Model Loaded and initalized");

		String fileDir = "C:/captures/";
		ArrayList<String> files = new ArrayList<String>();

		files.add("cat2.jpg");

		BufferedImage imgInput = null;
		for(String fileName :files) {
			try {
				imgInput = ImageIO.read(new File(fileDir+fileName));
				doPrediction(imgInput);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}

	public ArrayList<Classification> doPrediction(BufferedImage imgInput) {
		ArrayList<Classification> results = new ArrayList<Classification>();		
		
		NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
		INDArray imageTensor = null;
		try {
			imageTensor = loader.asMatrix(imgInput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataNormalization scaler = new VGG16ImagePreProcessor();
		scaler.transform(imageTensor);
		INDArray[] output = this.graphNet.output(imageTensor);

		results = this.decodePredictions(output[0]);


		return results;
	}

}
