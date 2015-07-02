package algorithms.PSO;

import java.util.HashMap;
import java.util.Random;

import algorithms.common.IEvolutionaryAlgorithm;
import algorithms.common.Individual;
import algorithms.common.Operation;
import algorithms.common.Dimension;
import functionParsing.RPNEvaluator;
import org.jfree.data.xy.AbstractXYZDataset;
import org.jfree.data.xy.DefaultXYZDataset;


public class Swarm implements IEvolutionaryAlgorithm {
	private Particle[] particles;
    private HashMap<String, Double> bestKnownPosition;
    private final HashMap<String, PSODimension> dimensions;
    private double bestKnownValue;
    public final Operation operation;
    private final double C1;
    private final double C2;
    private Random rand;
    private final String functionRPN;
    
    
	public Swarm(String functionRPN, int particlesNumber, HashMap<String, Dimension> dimensions, Operation operation, double C1, double C2) throws Exception{
        this.functionRPN = functionRPN;
        
        this.dimensions = new HashMap<>();
        for(String key : dimensions.keySet()){
            this.dimensions.put(key, new PSODimension(dimensions.get(key).getMinBoundary(), dimensions.get(key).getMaxBoundary()));
        }
        
        this.initBestKnownPositionAndValue();
        this.initParticles(particlesNumber);
        this.operation = operation;
        this.C1 = C1;
        this.C2 = C2;
        rand = new Random();
    }
	
	public double fitnessFunction(HashMap<String, Double> vars) throws Exception{
		String expr = this.functionRPN;
		for(String key : vars.keySet()){
			expr = expr.replaceAll(key, RPNEvaluator.df.format(vars.get(key)));
		}
		double res = RPNEvaluator.evalRPN(expr); 
		return res;
	}
	
    public Particle[] getParticles() {
		return particles;
	}
	public HashMap<String, Double> getBestKnownPosition() {
		return bestKnownPosition;
	}
	public double getBestKnownPosition(String key) {
		return bestKnownPosition.get(key);
	}
	public double getBestKnownValue() {
		return bestKnownValue;
	}
	public Operation getOperation(){
		return operation;
	}
	
	@SuppressWarnings("unchecked")
	public void updateBestKnownPositionAndValue(Particle particle){
		if(this.operation == Operation.Maximize)
		{
			if(particle.getBestKnownValue() > this.bestKnownValue){
				this.bestKnownPosition = (HashMap<String, Double>)particle.getBestKnownPosition().clone();
				this.bestKnownValue = particle.getBestKnownValue();
			}
		}
		else{
			if(particle.getBestKnownValue() < this.bestKnownValue){
				this.bestKnownPosition = (HashMap<String, Double>)particle.getBestKnownPosition().clone();
				this.bestKnownValue = particle.getBestKnownValue();
			}
		}
	}
	
	private Particle newPartcle(HashMap<String, Double> position, HashMap<String, Double> velocity) throws Exception{
		Particle particle = new Particle(position, velocity, this.fitnessFunction(position), this.operation);
		this.updateBestKnownPositionAndValue(particle);
		return particle;
	}
	
	private void initBestKnownPositionAndValue() throws Exception{
		HashMap<String, Double> position = new HashMap<String, Double>();
		for (String key : this.dimensions.keySet()) {
			
			PSODimension dimension = dimensions.get(key);
			position.put(key, ((dimension.getMinBoundary() + dimension.getMaxBoundary())/2.0));
		}
		
        this.bestKnownPosition = position;
        this.bestKnownValue = fitnessFunction(position);
	}
	private void initParticles(int particlesNumber) throws Exception{
        Random rand = new Random();
        this.particles = new Particle[particlesNumber];
        
        for (int i = 0; i < particlesNumber; ++i) {
        	
            HashMap<String, Double> position = new HashMap<String, Double>();
            HashMap<String, Double> velocity = new HashMap<String, Double>();
        	
        	for (String key : this.dimensions.keySet()) {
        		PSODimension dimension = dimensions.get(key);
	        	position.put(key, (rand.nextDouble() * (dimension.getMaxBoundary() - dimension.getMinBoundary()) + dimension.getMinBoundary()));
				
				velocity.put(key, 0.0);
			}
        	
        	particles[i] = this.newPartcle(position, velocity);
        }
	}
	
	public void makeIteration(){

		for(int i = 0; i < this.getParticles().length; ++i){
			
			Particle particle = this.getParticles()[i];
			
			for (String key : particle.getVelocity().keySet()) {
				
				double maxVelocity = dimensions.get(key).getMaxVelocity();
				double newVelocity = particle.getVelocity(key) + 
						C1 * rand.nextDouble() * (particle.getBestKnownPosition(key) - particle.getPosition(key)) + 
						C2 * rand.nextDouble() * (this.getBestKnownPosition(key) - particle.getPosition(key));
				
				if(newVelocity > maxVelocity) newVelocity = maxVelocity;
				else if(newVelocity < (maxVelocity * -1)) newVelocity = -maxVelocity;
				
				particle.setVelocity(key, newVelocity); 
			}
			
			HashMap<String, Double> newPosition = new HashMap<String, Double>();
			for(String key : particle.getBestKnownPosition().keySet()){
				double newPositionInDimension = particle.getPosition(key) + particle.getVelocity(key);
				
				if(newPositionInDimension > dimensions.get(key).getMaxBoundary()) newPositionInDimension = dimensions.get(key).getMaxBoundary();
				if(newPositionInDimension < dimensions.get(key).getMinBoundary()) newPositionInDimension = dimensions.get(key).getMinBoundary();
				
				newPosition.put(key, newPositionInDimension);
			}
			try {
				particle.setCurrentPosition(newPosition, this.fitnessFunction(newPosition));
			} catch (Exception e) {}
			this.updateBestKnownPositionAndValue(particle);
		}
	}

	@Override
	public Individual[] getPopulation() {
		return this.particles;
	}

	@Override
	public double getBestValue() {
		return this.getBestKnownValue();
	}

	@Override
	public HashMap<String, Double> getBestPosition() {
		return getBestKnownPosition();
	}

	@Override
	public DefaultXYZDataset getDataSet() {
            try{
                double[][] result = new double[3][this.particles.length];
                for(int i = 0; i < this.particles.length; ++i){

                    result[0][i] = this.particles[i].getPosition("x1");
                    result[1][i] = this.particles[i].getPosition("x1");
                    result[2][i] = fitnessFunction(this.particles[i].getPosition());

                }
                DefaultXYZDataset xyzDataSet = new DefaultXYZDataset();
                xyzDataSet.addSeries("mainData", result);
                return xyzDataSet;
            } catch(Exception ex){
                return null;
            }
	}
}
