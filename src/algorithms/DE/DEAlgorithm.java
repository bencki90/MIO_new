package algorithms.DE;

import java.util.HashMap;
import java.util.Random;

import algorithms.PSO.PSODimension;
import algorithms.common.Dimension;
import algorithms.common.IEvolutionaryAlgorithm;
import algorithms.common.Individual;
import algorithms.common.Operation;
import functionParsing.RPNEvaluator;
import org.jfree.data.xy.DefaultXYZDataset;

public class DEAlgorithm implements IEvolutionaryAlgorithm{
	private final HashMap<String, Dimension> dimensions;
	private final double F;
	private final double CR;
	private Individual[] population;
	
	private final String functionRPN;
	private final Operation operation;
	
	private Random random;
	
	public DEAlgorithm(String functionRPN, final int populationSize, final HashMap<String, Dimension> dimensions, final Operation operation, final double F, final double CR) {
		this.functionRPN = functionRPN;
		this.dimensions = dimensions;
		this.operation = operation;
		this.F = F;
		this.CR = CR;

		this.random = new Random();
		this.InitializePopulation(populationSize);
	}


	private void InitializePopulation(final int populationSize) {
		
		this.population = new Individual[populationSize];
		
		for(int i = 0; i < populationSize; ++i){
			
			HashMap<String, Double> position = new HashMap<String, Double>();
			
			for (String key : this.dimensions.keySet()) {
        		Dimension dimension = dimensions.get(key);
        		
	        	position.put(key, (random.nextDouble() * (dimension.getMaxBoundary() - dimension.getMinBoundary()) + dimension.getMinBoundary()));
			}
			
			this.population[i] = new Individual(position);
		}
	}
	
	private void makeEvolution() throws Exception {
		for(int i = 0; i < this.population.length; ++i) {
			
			int x = (int) (random.nextDouble() * (double) (this.population.length - 1));
			int a, b, c;
			
			do{
				a = (int) (random.nextDouble() * (double) (this.population.length - 1));
			}while(a == x);
			
			do{
				b = (int) (random.nextDouble() * (double) (this.population.length - 1));
			}while(b == x || b == a);
			
			do{
				c = (int) (random.nextDouble() * (double) (this.population.length - 1));
			}while(c == x || c == a || c == b);
			
			int R = random.nextInt(this.dimensions.size() + 1);
			
			Individual individualCandidate = new Individual(this.population[x]);
			
			
			
			for(String key : this.dimensions.keySet()){
				if(random.nextInt(this.dimensions.size() + 1) == R || random.nextDouble() * 1 < this.CR) {
					
					individualCandidate.setPosition(key, this.population[a].getPosition(key) + this.F * (this.population[b].getPosition(key) - this.population[c].getPosition(key)));
				}
			}
                        
                        boolean candidateInBoundaries = true;
                        for(String key : dimensions.keySet()){
                            if(individualCandidate.getPosition(key) > dimensions.get(key).getMaxBoundary() ||
                                    individualCandidate.getPosition(key) < dimensions.get(key).getMinBoundary()){
                                candidateInBoundaries = false;
                                break;
                            }
                        }
                        if(!candidateInBoundaries) continue;
                        
			if(this.operation == Operation.Minimize){
				if(this.fitnessFunction(this.population[x].getPosition()) > this.fitnessFunction(individualCandidate.getPosition())){
					this.population[x] = individualCandidate;
				}
			}
			else{
				if(this.fitnessFunction(this.population[x].getPosition()) < this.fitnessFunction(individualCandidate.getPosition())){
					this.population[x] = individualCandidate;
				}
			}
			
		}
	}
	
	private double fitnessFunction(HashMap<String, Double> vars) throws Exception{
		String expr = this.functionRPN;
		for(String key : vars.keySet()){
			expr = expr.replaceAll(key, RPNEvaluator.df.format(vars.get(key)));
		}
		
		return RPNEvaluator.evalRPN(expr);
	}
	
	private HashMap<String, Double> bestPosition() throws Exception{
		HashMap<String, Double> best = this.population[0].getPosition();
		for(int i = 1; i < this.population.length; ++i){
			if(this.operation == Operation.Minimize){
				if(this.fitnessFunction(this.population[i].getPosition()) < this.fitnessFunction(best)){
					best = this.population[i].getPosition();
				}
			}
			else{
				if(this.fitnessFunction(this.population[i].getPosition()) > this.fitnessFunction(best)){
					best = this.population[i].getPosition();
				}
			}
		}
		return best;
	}
	
	private double bestValue() throws Exception{
		return this.fitnessFunction(this.bestPosition());
	}
	
	public void printResults() throws Exception{
		System.out.println("############ RESULT ############");
		for(int i = 0; i < this.population.length; ++i){
			System.out.print("Individual " + (i + 1) + ": ");

			for(String key : this.population[i].getPosition().keySet()){
				System.out.format(key + ": %f.6\t", this.population[i].getPosition(key));
			}
			System.out.println();
		}
	}


	@Override
	public void makeIteration() {
		try{
			this.makeEvolution();
		}
		catch(Exception ex) {}
	}


	@Override
	public Individual[] getPopulation() {
		return this.population;
	}


	@Override
	public double getBestValue() {
		try{
			return this.bestValue();
		}
		catch(Exception ex) {
			return Double.NaN;
		}
	}


	@Override
	public HashMap<String, Double> getBestPosition() {
		try {
			return this.bestPosition();
		} catch (Exception e) { 
			return null;
		}
	}
        
        @Override
	public DefaultXYZDataset getDataSet() {
            try{
                double[][] result = new double[3][this.population.length];
                for(int i = 0; i < this.population.length; ++i){

                    result[0][i] = this.population[i].getPosition("x1");
                    result[1][i] = this.population[i].getPosition("x1");
                    result[2][i] = fitnessFunction(this.population[i].getPosition());

                }
                DefaultXYZDataset xyzDataSet = new DefaultXYZDataset();
                xyzDataSet.addSeries("mainData", result);
                return xyzDataSet;
            } catch(Exception ex){
                return null;
            }
	}
}
