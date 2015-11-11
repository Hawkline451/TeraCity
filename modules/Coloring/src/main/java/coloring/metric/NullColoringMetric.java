package coloring.metric;

import coloring.BlockColors;

public class NullColoringMetric implements IColoringMetric {
	
	public NullColoringMetric() {
		super();
	}

	@Override
	public double getValue() {
		return -1;
	}

	@Override
	public void setValue(double value) {
		// cannot modify value
	}

	@Override
	public String getColor() {
		return BlockColors.BLOCK_NOTFOUND.toString();
	}
	
}
