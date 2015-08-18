package metrics;

import java.util.Map;

public class CBOCounterMetric extends CouplingCounterMetric {

	public CBOCounterMetric(Map<String, Integer> counter) {
		super(counter);
	}

}
