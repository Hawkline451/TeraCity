package metrics;

import java.util.Map;

abstract public class CounterMetric implements Metric {
	protected Map<String, Integer> counter;
	protected ThresholdColorGetter getter;
	public CounterMetric(Map<String, Integer> counter, ThresholdColorGetter getter) {
		this.counter = counter;
		this.getter = getter;
	}
	@Override
	public String getColor(String classPath) 
	{
		Integer warnings = counter.get(classPath);
		return getter.getColor(warnings);
	}
}
