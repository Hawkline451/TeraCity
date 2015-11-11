package coloring.metric;

public class RateColoringMetric implements IColoringMetric {
	
	private double rate = 0.0;
	public static final double BEST_RATE = 1.0;
	public static final double WORST_RATE = 0.0;
	
	public RateColoringMetric(double rate) {
		setValue(rate);
	}

	@Override
	public double getValue() {
		return this.rate;
	}

	@Override
	public void setValue(double value) {
		value = Math.max(0.0, value);
		value = Math.min(1.0, value);
		this.rate = value;
	}

	@Override
	public String getColor() {
		
		if (rate <= 0.2) { return "red";    }
    	if (rate <= 0.4) { return "orange"; }
    	if (rate <= 0.6) { return "yellow"; }
    	if (rate <= 0.8) { return "blue";   }
    	return "green";
	}
	
}
