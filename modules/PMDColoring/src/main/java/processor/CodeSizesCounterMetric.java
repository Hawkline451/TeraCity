package processor;

import java.util.Map;

public class CodeSizesCounterMetric implements Metric {
	
	private final int [] WARNING_LIMITS = {2, 5};
	private Map<String, Integer> counter;
	
	public CodeSizesCounterMetric(Map<String, Integer> counter) {
		this.counter = counter;
	}

	@Override
	public String getColor(String classPath) 
	{
		Integer warnings = counter.get(classPath);
		if (warnings == null) warnings = 0;
		
		if(warnings <= WARNING_LIMITS[0]) return "Green";
		else if (warnings <= WARNING_LIMITS[1]) return "Yellow";
		return "Red";
	}

}
