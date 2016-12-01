package org.terasology.codecity.world.map;

import java.util.Arrays;

import org.terasology.codecity.world.structure.scale.CodeScaleManager;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;

/*
 *  Blocks that index code in the building 
 */
public class IndexCodeBlockFactory {
	
/**
 * Returns the block associated with a block with a  binary representation
 * @param block      Binary representation of the code chunk
 * @return           Block representing that chunk
 */
	
  //TODO add invisible block
  private static Block default_block = CoreRegistry.get(BlockManager.class).getBlock("core:Ice");
	
	//TODO generate a partir de codigo
//   public static Block generate(DrawableCode code,int row,int column, int[][] sliceBin) {
	   public static Block generate(int[][] sliceBin) {
	
	if(sliceBin == null)
		return null;
	   
	//if the block contains code   
	if(sliceBin[0][0] != 0 || sliceBin[0][1] != 0 ||
	   sliceBin[1][0] != 0 || sliceBin[1][1] != 0 ){
		return default_block;
	}
	  
    return null;

  }
   
	   
  @Deprecated
	public static Block generate(int[] lineLength, int blockNum) {
	  return CoreRegistry.get(BlockManager.class).getBlock("core:lava");
	}

/** 
 * Adds to the hash table of the portion of the code
 * @param position
 * @param code
 * @param row
 * @param col
 */
public static void addBlockCode(Vector3i position, DrawableCode code, int row, int col) {
	// TODO Auto-generated method stub
	
}
}
