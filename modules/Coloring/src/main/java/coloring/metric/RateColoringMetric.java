package coloring.metric;

import coloring.BlockColors;

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
		
		if (rate <= 0.2) { return BlockColors.BLOCK_RED.toString();    }
    	if (rate <= 0.4) { return BlockColors.BLOCK_ORANGE.toString(); }
    	if (rate <= 0.6) { return BlockColors.BLOCK_YELLOW.toString(); }
    	if (rate <= 0.8) { return BlockColors.BLOCK_BLUE.toString();   }
    	return BlockColors.BLOCK_GREEN.toString();
	}
	
}
