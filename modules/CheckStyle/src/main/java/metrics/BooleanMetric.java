package metrics;

public class BooleanMetric extends Metric {
	public BooleanMetric(int max) {
		path += "booleanRule.xml";
		this.max = max;
	}
}