package metrics;

import java.util.Map;

public class EICounterMetric extends CouplingCounterMetric {

	public EICounterMetric(Map<String, Integer> counter) {
		super(counter);
	}

}
