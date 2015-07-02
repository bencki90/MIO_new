package algorithms.PSO;

import java.util.HashMap;

import algorithms.common.Operation;

public class Particle extends algorithms.common.Individual {
	private HashMap<String, Double> bestKnownPosition;
	private double bestKnownValue;
	private HashMap<String, Double> velocity;
	private Operation operation;
	

	public Particle(HashMap<String, Double> position, HashMap<String, Double> velocity, double value, Operation operation){
		super(position);
		this.bestKnownPosition = position;
		this.bestKnownValue = value;
		this.velocity = velocity;
		this.operation = operation;
	}
	
	public double getBestKnownValue() {
		return bestKnownValue;
	}

	public void setCurrentPosition(HashMap<String, Double> newPosition, double newValue) {
		this.currentPosition = newPosition;

		if(this.operation == Operation.Maximize){
			if(newValue > this.bestKnownValue){
				this.bestKnownValue = newValue;
				this.bestKnownPosition = newPosition;
			}
		}
		else{
			if(newValue < this.bestKnownValue){
				this.bestKnownValue = newValue;
				this.bestKnownPosition = newPosition;
			}
		}
	}

	public HashMap<String, Double> getBestKnownPosition() {
		return bestKnownPosition;
	}
	public double getBestKnownPosition(String key) {
		return bestKnownPosition.get(key);
	}
	
	public HashMap<String, Double> getVelocity() {
		return velocity;
	}
	public double getVelocity(String key) {
		return velocity.get(key);
	}

	public void setVelocity(HashMap<String, Double> velocity) {
		this.velocity = velocity;
	}
	public void setVelocity(String key, double value) {
		this.velocity.put(key, value);
	}
}
