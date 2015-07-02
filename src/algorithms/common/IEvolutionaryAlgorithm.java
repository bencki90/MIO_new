package algorithms.common;

import java.util.HashMap;
import org.jfree.data.xy.DefaultXYZDataset;


public interface IEvolutionaryAlgorithm {
	public void makeIteration();
	public Individual[] getPopulation();
	public double getBestValue();
	public HashMap<String, Double> getBestPosition();
        public DefaultXYZDataset getDataSet();
}
