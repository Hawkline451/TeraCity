package org.terasology.codecity.world.structure.metric;

import org.terasology.codecity.world.structure.CodeClass;

/**
 * Gets number of method calls in class (using ast).
 * @author paula
 *
 */
public class NumberOfMethodCallsMetric extends CodeMetric {

	@Override
	public String getMetricVal(CodeClass code) {
		return Integer.toString(code.getAst().getMethodCalls().size());
	}

}
