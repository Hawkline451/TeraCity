package org.terasology.codecity.world.structure.metric;

import org.terasology.codecity.world.structure.CodeClass;

public class ClassLengthMetric extends CodeMetric {

	@Override
	public String getMetricVal(CodeClass code) {
		return Integer.toString(code.getAst().getLength());
	}

}
