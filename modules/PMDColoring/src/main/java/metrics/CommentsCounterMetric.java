package metrics;

import java.util.Map;

public class CommentsCounterMetric extends CounterMetric
{
	public CommentsCounterMetric(Map<String, Integer> counter) {
		super(counter, ThresholdColorGetter.COMMENTS_METRIC);
	}
}