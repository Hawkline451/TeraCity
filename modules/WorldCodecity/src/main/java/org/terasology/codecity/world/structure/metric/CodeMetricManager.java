package org.terasology.codecity.world.structure.metric;

import java.util.ArrayList;
import java.util.HashMap;

import org.terasology.codecity.world.structure.CodeClass;

/**
 * Manages the horizontal and vertical Metrics.
 * @author adderou
 *
 */
public class CodeMetricManager {

	private CodeMetric horizontal, vertical;
	private static HashMap<String,CodeMetric> MetricMap = new HashMap<>();
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
		this.horizontal = MetricMap.get("class-line-length");
		this.vertical = MetricMap.get("class-length");
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

	public static CodeMetric getMetricFromString(String Metric) {
		return MetricMap.get(Metric);
	}
	
	public ArrayList<String> getAvailableMetricNames() {
	    return MetricTypes;
	}
	
	private void registerCodeMetric(String MetricName, CodeMetric Metric) {
		MetricTypes.add(MetricName);
		MetricMap.put(MetricName, Metric);
	}
	
	public static String getInfoString(CodeClass code) {
		StringBuilder info  = new StringBuilder();
		info.append("Class: ");
		info.append(code.getName());
		info.append("\n");
		info.append("Length: ");					
		info.append(getMetricFromString("class-length").getMetricVal(code));
		info.append(" lines\n");
		info.append("Comments: ");					
		info.append(getMetricFromString("number-of-comments").getMetricVal(code));
		info.append("\n");
		info.append("Imports: ");					
		info.append(getMetricFromString("number-of-imports").getMetricVal(code));
		info.append("\n");
		info.append("Methods: ");					
		info.append(getMetricFromString("number-of-methods").getMetricVal(code));
		info.append("\n");
		info.append("Method Calls: ");					
		info.append(getMetricFromString("number-of-method-calls").getMetricVal(code));
		info.append("\n");
		info.append("Properties: ");					
		info.append(getMetricFromString("number-of-properties").getMetricVal(code));
		info.append("\n");
		// Cobertura Metrics
		info.append(new branchRate().specificFunction(code.getPath()));
		info.append("\n");
		info.append(new coverageAprox().specificFunction(code.getPath()));
		info.append("\n");
		info.append(new lineRate().specificFunction(code.getPath()));
		return info.toString();
	}
}
