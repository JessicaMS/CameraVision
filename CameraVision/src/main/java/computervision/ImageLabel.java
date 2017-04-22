package computervision;

import java.util.Observable;

public class ImageLabel extends Observable {
	private String labelName;
	private double probability;
	
	public ImageLabel() {
		labelName = "(None)";
		probability = 100.0;
	}
	
	public ImageLabel(String labelName, double probability) {
		this.labelName = labelName;
		this.probability =(probability);
	}
	
	/*
	 * If label is changed, update observers
	 */
	public void labelChanged()
	{
		setChanged(); 
		notifyObservers();
	}
	
	public String toString() {
		return this.labelName + ": " + String.format("%3f", probability) + "%"; 
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public String getLabelName() {
		return labelName;
	}
	
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public void setLabel(ImageLabel label2) {
		this.labelName = label2.getLabelName();
		this.probability = label2.getProbability();
		
		labelChanged();
	}
}
