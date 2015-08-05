package metrics;

public class CyclomaticMetric extends Metric {
	public CyclomaticMetric(int max) {
		path += "cyclomaticRule.xml";
		this.max = max;
	}
}
