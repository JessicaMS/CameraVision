package computervision;

public class ImageLabel {
	private String label;
	private double probability;
	
	ImageLabel(String label, double probability) {
		this.setLabel(label);
		this.setProbability(probability);
	}
	
	public String toString() {
		return String.format("%3f", probability) + "%, " + this.label; 
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
