package metrics;

public class FanOutMetric extends Metric {
	public FanOutMetric(int max) {
		path += "fanOutRule.xml";
		this.max = max;
	}
}
