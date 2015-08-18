package metrics;

import java.util.Map;

public class CodesizeCounterMetric extends CounterMetric {

	public CodesizeCounterMetric(Map<String, Integer> counter) {
		super(counter, ThresholdColorGetter.CODESIZE_METRIC);
	}

}
