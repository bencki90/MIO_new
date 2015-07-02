package algorithms.common;

import java.util.HashMap;

public class Individual {
	protected HashMap<String, Double> currentPosition;

	public Individual(HashMap<String, Double> position){
		this.currentPosition = position;
	}
	
	@SuppressWarnings("unchecked")
	public Individual(Individual anotherIndividual){
		this.currentPosition = (HashMap<String, Double>) anotherIndividual.currentPosition.clone();
	}
	
	public HashMap<String, Double> getPosition(){
		return this.currentPosition;
	}
	public double getPosition(String key) {
		return this.currentPosition.get(key);
	}
	
	public void setPosition(HashMap<String, Double> newPosition) {
		this.currentPosition = newPosition;
	}
	public void setPosition(String key, double value) {
		this.currentPosition.put(key, value);
	}
}
