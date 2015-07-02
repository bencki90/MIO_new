package algorithms.common;

public class Dimension {
	protected final double maxBoundary;
	protected final double minBoundary;
	
	public Dimension(final double minBoundary, final double maxBoundary){
		this.minBoundary = minBoundary;
		this.maxBoundary = maxBoundary;
	}
	
	public double getMaxBoundary() {
		return maxBoundary;
	}

	public double getMinBoundary() {
		return minBoundary;
	}
}
