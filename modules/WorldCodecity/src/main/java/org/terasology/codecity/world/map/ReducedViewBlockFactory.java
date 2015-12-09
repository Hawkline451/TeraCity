package org.terasology.codecity.world.map;

import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

/*
 * Only works with HalfLinearCodeScale
 */
public class ReducedViewBlockFactory {
	
	public static Block generate(int[] lineLength, int blockNum) {
		if (lineLength == null) {
			return CoreRegistry.get(BlockManager.class).getBlock("core:stone");
		}
		int[] reverse = new int[lineLength.length];
		for (int j = 0; j < lineLength.length; j++) {
			reverse[lineLength.length-1-j] = lineLength[j];
		}
		if (2*blockNum+1 < lineLength.length) {
			int secondLine = Math.min(reverse[2*blockNum]/6, 15);
			int firstLine = Math.min(reverse[2*blockNum+1]/6, 15);
			String blockName = ""+firstLine+"_"+secondLine;
			return CoreRegistry.get(BlockManager.class).getBlock("worldcodecity:Stone"+blockName);
		}
		return CoreRegistry.get(BlockManager.class).getBlock("core:stone");
	}
}
