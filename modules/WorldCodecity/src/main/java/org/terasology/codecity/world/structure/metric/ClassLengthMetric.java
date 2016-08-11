package org.terasology.codecity.world.structure.metric;

import org.terasology.codecity.world.structure.CodeClass;

/**
 * Gets class length from ast.
 * @author paula
 *
 */
public class ClassLengthMetric extends CodeMetric {

	@Override
	public String getMetricVal(CodeClass code) {
		return Integer.toString(code.getAst().getLength());
	}

}
