package metrics;

public class DataAbstractionCouplingMetric extends Metric {
	public DataAbstractionCouplingMetric(int max) {
		path += "dataAbstractionCouplingRule.xml";
		this.max = max;
	}
}
