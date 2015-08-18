package metrics;

import java.util.Map;

public class CouplingCounterMetric extends CounterMetric {

	public CouplingCounterMetric(Map<String, Integer> counter) {
		super(counter, ThresholdColorGetter.COUPLING_METRIC);
	}

}
