package org.terasology.codecity.world.structure.metric;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Manages the horizontal and vertical Metrics.
 * @author adderou
 *
 */
public class CodeMetricManager {

	private CodeMetric horizontal, vertical;
	private HashMap<String,CodeMetric> MetricMap = new HashMap<>();
	private ArrayList<String> MetricTypes = new ArrayList<>();

	
	/**
	 * Default constructor.
	 */
	public CodeMetricManager() {
		
		registerCodeMetric("class-line-length",new ClassLineLengthMetric());
		registerCodeMetric("class-length",new ClassLengthMetric());
		registerCodeMetric("number-of-comments",new NumberOfCommentsMetric());
		registerCodeMetric("number-of-imports",new NumberOfImportsMetric());
		registerCodeMetric("number-of-method-calls",new NumberOfMethodCallsMetric());
		registerCodeMetric("number-of-methods", new NumberOfMethodsMetric());
		registerCodeMetric("number-of-properties", new NumberOfPropertiesMetric());
		
		registerCodeMetric("branchRate",new branchRate());
		registerCodeMetric("lineRate",new lineRate());
		registerCodeMetric("coverageAprox",new coverageAprox());
		this.horizontal = MetricMap.get("square-root");
		this.vertical = MetricMap.get("linear");
	}
	
	public CodeMetricManager(CodeMetric horizontal, CodeMetric vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	
	public void setVerticalMetric(CodeMetric vertical) {
		this.vertical = vertical;
	}
	
	public void setHorizontalMetric(CodeMetric horizontal) {
		this.horizontal = horizontal;
	}
	
	public CodeMetric getVerticalMetric() {
		return vertical;
	}
	
	public CodeMetric getHorizontalMetric() {
		return horizontal;
	}

	public CodeMetric getMetricFromString(String Metric) {
		return MetricMap.get(Metric);
	}
	
	public ArrayList<String> getAvailableMetricNames() {
	    return MetricTypes;
	}
	
	private void registerCodeMetric(String MetricName, CodeMetric Metric) {
		MetricTypes.add(MetricName);
		MetricMap.put(MetricName, Metric);
	}
}
