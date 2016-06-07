package org.terasology.codecity.world.structure;

import java.util.Random;

public class DummyArray {
	public static int[] getArray(int length) {
		int[] linesLength =  new int[length];
		for (int i=0;i<length;i++) {
			linesLength[i] = new Random().nextInt(100);
		}
		return linesLength;
	}
}
