package metrics;

public class NPathMetric extends Metric {
	public NPathMetric(int max) {
		path += "nPathRule.xml";
		this.max = max;
	}
}
