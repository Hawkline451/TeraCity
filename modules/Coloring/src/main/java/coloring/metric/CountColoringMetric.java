package coloring.metric;

public class CountColoringMetric implements IColoringMetric {
	
	private int count = 0;
	private int limit = 100;
	
	public CountColoringMetric(int count, int limit) {
		setValue(count);
		setLimit(limit);
	}
	
	public CountColoringMetric(double count, int limit) {
		setValue(count);
		setLimit(limit);
	}

	@Override
	public double getValue() {
		return this.count;
	}

	@Override
	public void setValue(double value) {
		value = Math.max(0, value);
		this.count = (int)Math.round(value);
	}

	@Override
	public String getColor() {
		
		double limited = Math.min(this.limit, this.count);
		double rate = 1 - limited/(double)limit;
		
		if (rate <= 0.2) { return "red";    }
    	if (rate <= 0.4) { return "orange"; }
    	if (rate <= 0.6) { return "yellow"; }
    	if (rate <= 0.8) { return "blue";   }
    	return "green";
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		limit = Math.max(0, limit);
		this.limit = limit;
	}
	
}
