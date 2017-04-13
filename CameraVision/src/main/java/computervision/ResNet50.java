package computervision;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.Utils.ImageNetLabels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;

public class ResNet50 implements ClassificationStrategy {

	private static final String modelJsonFilepath = "./nnmodel/";
	private static final String weightsHdf5Filepath = "./nnmodel/";
	private ComputationGraph graphNet;
	
	public ResNet50() {
		this.loadNetwork();
	}

	public ArrayList<ImageLabel> decodePredictions(INDArray predictions) {
		ArrayList<String> labels;
		ArrayList<ImageLabel> results = new ArrayList<ImageLabel>();

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
				results.add(new ImageLabel( labels.get(top5[i]),  top5Prob[i] * 100 ) );
				i++;
			}
		}
		return results;
	}

	private void loadNetwork() {

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

		String fileDir = "./nnmodel/cat2.jpg";

		BufferedImage imgInput = null;
		try{
			imgInput = ImageIO.read(new File(fileDir));
			scanImage(imgInput);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private void displayPredictions(ArrayList<ImageLabel> predictions) {
		System.out.println( "Predictions for batch :");
		for(ImageLabel pred:  predictions) {
			System.out.println(pred.toString());
		}
	}
	
	@Override
	public ImageLabel scanImage(BufferedImage imgInput) {
		ArrayList<ImageLabel> results = new ArrayList<ImageLabel>();		

		NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
		INDArray imageTensor = null;
		try {
			imageTensor = loader.asMatrix(imgInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		DataNormalization scaler = new VGG16ImagePreProcessor();
		scaler.transform(imageTensor);
		INDArray[] output = this.graphNet.output(imageTensor);

		results = this.decodePredictions(output[0]);


		return results.get(0);
	}

}
