package computervision;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.shade.jackson.databind.ObjectMapper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class GregggResNet implements ImageProcessor {
	private static final String modelJsonFilepath = "./nnmodel/";
	private static final String weightsHdf5Filepath = "./nnmodel/";
    private static ArrayList<String> labels = null;
	
    private ComputationGraph graphNet = null;
	
    public GregggResNet() {
    	this.loadNetwork();
    }

	//	private static Logger log = LoggerFactory.getLogger();
	public ArrayList<String> getLabels() {
        if (labels == null) {
            HashMap<String, String> jsonMap;
            try {
                jsonMap = new ObjectMapper().readValue(new File("./nnmodel/class_index.json"), HashMap.class);
                labels = new ArrayList<>(jsonMap.size());
                for (int i = 0; i < jsonMap.size(); i++) {
                    labels.add(jsonMap.get(String.valueOf(i)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return labels;
}

	public static ArrayList<ImageLabel> decodePredictions(INDArray predictions) {
		ArrayList<ImageLabel> results = new ArrayList<ImageLabel>();
		//String predictionDescription = "";
		int[] top5 = new int[5];
		float[] top5Prob = new float[5];

		int i = 0;
		for (int batch = 0; batch < predictions.size(0); batch++) {
			INDArray currentBatch = predictions.getRow(batch).dup();
			while (i < 5) {
				top5[i] = Nd4j.argMax(currentBatch, 1).getInt(0, 0);
				top5Prob[i] = currentBatch.getFloat(batch, top5[i]);
				currentBatch.putScalar(0, top5[i], 0);
				//predictionDescription += "\n\t" + String.format("%3f", top5Prob[i] * 100) + "%, " + labels.get(top5[i]);
				results.add(new ImageLabel( labels.get(top5[i]),  top5Prob[i] * 100 ) );
				i++;
			}
		}
		return results;
	}


	private void loadNetwork(){
		String modelJsonFilename = modelJsonFilepath + "signs_res.json";
		String weightsHdf5Filename = weightsHdf5Filepath + "signs_res.h5";

		this.labels = this.getLabels();
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

			scanImage(imgInput);

		}
	}

	@Override
	public ImageLabel scanImage(BufferedImage imgInput) {
		ArrayList<ImageLabel> results = new ArrayList<ImageLabel>();		
		INDArray imageTensor = Image2Tensor.image2Tensor(imgInput, 224, 224, 3);
		
		DataNormalization scaler = new ImagePreProcessingScaler(0, 1);
		scaler.transform(imageTensor);
		INDArray[] output = this.graphNet.output(imageTensor);

		results = (decodePredictions(output[0]));
		System.out.println(results);
		
		return results.get(0);

	}



}
