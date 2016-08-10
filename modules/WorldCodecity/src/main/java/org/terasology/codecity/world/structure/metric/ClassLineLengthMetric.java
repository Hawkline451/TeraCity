package org.terasology.codecity.world.structure.metric;

import org.terasology.codecity.world.structure.CodeClass;

/**
 * Gets max line length from class (using ast).
 * @author paula
 *
 */
public class ClassLineLengthMetric extends CodeMetric {

	@Override
	public String getMetricVal(CodeClass code) {
		return Integer.toString(getMax(code.getAst().getLinesLength()));
	}

	public static int getMax(int[] inputArray) {
		int maxValue = inputArray[0];
		for (int i = 1; i < inputArray.length; i++) {
			if (inputArray[i] > maxValue) {
				maxValue = inputArray[i];
			}
		}
		return maxValue;
	}

}
