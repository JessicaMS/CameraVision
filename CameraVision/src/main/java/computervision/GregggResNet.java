package computervision;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.trainedmodels.Utils.ImageNetLabels;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.dataset.api.preprocessor.VGG16ImagePreProcessor;
import org.nd4j.linalg.factory.Nd4j;
//import org.datavec.image.loader.CifarLoader;

import Image2Tensor.Image2Tensor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class GregggResNet {
	private static final String modelJsonFilepath = "./nnmodel/";
	private static final String weightsHdf5Filepath = "./nnmodel/";
	ComputationGraph graphNet = null;

	//	private static Logger log = LoggerFactory.getLogger();


	public static String decodePredictions(INDArray predictions) {
		ArrayList<String> labels = new ArrayList<String>();
		String predictionDescription = "";
		int[] top5 = new int[5];
		float[] top5Prob = new float[5];

		//labels = ImageNetLabels.getLabels();
		labels.add("224"); 
		labels.add("227-230"); 
		labels.add("231"); 
		labels.add("275"); 
		labels.add("276"); 
		labels.add("278"); 
		labels.add("A235"); 
		labels.add("csdept"); 
		labels.add("rescue");

		//brute force collect top 5
		int i = 0;
		for (int batch = 0; batch < predictions.size(0); batch++) {
			predictionDescription += "Predictions for batch ";
			if (predictions.size(0) > 1) {
				predictionDescription += String.valueOf(batch);
			}
			predictionDescription += " :";
			INDArray currentBatch = predictions.getRow(batch).dup();
			while (i < 5) {
				top5[i] = Nd4j.argMax(currentBatch, 1).getInt(0, 0);
				top5Prob[i] = currentBatch.getFloat(batch, top5[i]);
				currentBatch.putScalar(0, top5[i], 0);
				predictionDescription += "\n\t" + String.format("%3f", top5Prob[i] * 100) + "%, " + labels.get(top5[i]);
				i++;
			}
		}
		return predictionDescription;
	}



	public void loadGregggResNet(){
		String modelJsonFilename = modelJsonFilepath + "signs_res.json";
		String weightsHdf5Filename = weightsHdf5Filepath + "signs_res.h5";


		try {

			graphNet = KerasModelImport.importKerasModelAndWeights(modelJsonFilename, weightsHdf5Filename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		graphNet.init();
		System.out.println("Model Loaded and initalized");


//		try {
//			runTests(graphNet);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}



	}

	public void runTests(ComputationGraph graphNet) throws IOException {
		String fileDir = "./nnmodel/testdata/";
		ArrayList<String> files = new ArrayList<String>();

		files.add("A235_set3_203.jpg");
		files.add("csdept_set2_276.jpg");
		files.add("227-230_set2_542.jpg");
		files.add("224.jpg");
		files.add("224_near.jpg");
		files.add("227.jpg");
		files.add("227_far.jpg");
		files.add("227_mid.jpg");
		files.add("275.jpg");
		files.add("276_close.jpg");
		files.add("A235.jpg");
		files.add("278_close.jpg");
		files.add("278_far.jpg");
		files.add("a235_close.jpg");
		files.add("a235_mid.jpg");
		files.add("cs_mid.jpg");
		files.add("csdept_near.jpg");
		files.add("rescue_close.jpg");
		files.add("rescue_far.jpg");


		for(String fileName :files) {
			BufferedImage imgInput = null;
			imgInput = ImageIO.read(new File(fileDir+fileName));

			INDArray imageTensor = Image2Tensor.image2Tensor(imgInput, 224, 224, 3);

			long start = System.currentTimeMillis();
			//NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
			//INDArray imageTensor = loader.asMatrix(imgInput);
			DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
			scaler.transform(imageTensor);
			//System.out.println(imageTensor);
			INDArray[] output = graphNet.output(imageTensor);
			System.out.println(output[0].toString());
			System.out.println(decodePredictions(output[0]));
			long elapsed = System.currentTimeMillis()-start;
			System.out.println("Prediction took " + elapsed + "milliseconds");

		}
	}

	public ArrayList<Classification> doPrediction(BufferedImage imgInput) {
		ArrayList<Classification> results = new ArrayList<Classification>();		

		//NativeImageLoader loader = new NativeImageLoader(224, 224, 3);
		INDArray imageTensor = null;
		imageTensor = Image2Tensor.image2Tensor(imgInput, 224, 224, 3);
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		scaler.transform(imageTensor);
		INDArray[] output = this.graphNet.output(imageTensor);

		//results = this.decodePredictions(output[0]);
		System.out.println(output[0]);

		return results;

	}
}
