package metrics;

import java.util.ArrayList;

public class NullMetric extends Metric {
	public NullMetric() {
	}
	
	@Override
	public boolean execute(String pathFile) {
		return false;
	}
	
	@Override
	public void setMetricValue(Integer max, ArrayList<String> lines) {
	}
	
	@Override
	public ArrayList<String> getMetricText() {
		return null;
	}
}
