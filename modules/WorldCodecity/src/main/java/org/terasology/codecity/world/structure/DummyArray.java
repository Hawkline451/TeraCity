package org.terasology.codecity.world.structure;

import java.util.Random;

public class DummyArray {
	public static Integer[] getArray(int length) {
		Integer[] linesLength =  new Integer[length];
		for (int i=0;i<length;i++) {
			linesLength[i] = new Random().nextInt(100);
		}
		return linesLength;
	}
}
