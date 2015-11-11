package coloring.metric;

public interface IColoringMetric {
	
	// returns the metric value as a problem rate in range [0,1]. 1.0 means no errors
	public double getValue();

	public void setValue(double value);
	
	public String getColor();

}