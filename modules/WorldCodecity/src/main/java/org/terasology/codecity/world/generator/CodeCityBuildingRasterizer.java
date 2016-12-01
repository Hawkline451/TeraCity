package org.terasology.codecity.world.generator;

import java.util.Arrays;
import java.util.Collections;

import org.terasology.codecity.world.facet.CodeCityFacet;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.IndexCodeBlockFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.map.ReducedViewBlockFactory;
import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

/**
 * Rasterizes buildings using the information provided by CodeCityFacet
 */
public class CodeCityBuildingRasterizer implements WorldRasterizer {
    private Block block;	

    @Override
    public void initialize() {
        block = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
    }
    
    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        CodeCityFacet codeCityFacet = chunkRegion
                .getFacet(CodeCityFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {
        	if(codeCityFacet.containsBlock(position)){
        		
        		MapObject map = codeCityFacet.getBlockType(position.x, position.y, position.z);
        		DrawableCode code = map.getObject();
        		
        		
        		

        		if (map.getColumn() != -1 ){
            		int row = map.getMaxY()-position.y;
            		int col = map.getColumn();
            		
            		int[][] sliceBin = ReducedViewBlockFactory.recalcBinary(code,row,col);
            		block = ReducedViewBlockFactory.generate(sliceBin);
            		//HERE GOES THE NEW FACTORY THAT TRANSLATE BLOQUE TO THE CORRENT BLOCK
            		
            		// Generate the invisible block related to the block code if it has code
            		if (map.isIndexBlock()){
//            			if (!block.getURI().toString().matches("worldcodecity:Stone[01]+")){
//        				if (!block.getURI().toString().contains("1")){	
//            			if(!sliceHasCode(sliceBin)){
//            				continue;
//            			}
            			block = CoreRegistry.get(BlockManager.class).getBlock("core:Ice");
            		}
            	    
        		}
        		else{ //Here block for borders which have map.getColumn() == -1
        			block = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
        		}
        		chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z), block);
        		
//        		if(block.getURI().toString().contains("worldcodecity")){
//        			Block lava_block = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
//        			chunk.setBlock(ChunkMath.calcBlockPos(position.x+1, position.y, position.z), lava_block);
//        		}
//        		
//        		if (indexBlock != null && block.getURI().toString().contains("worldcodecity:Stone")){
//        			
//        			chunk.setBlock(ChunkMath.calcBlockPos(position.x+1, position.y, position.z), indexBlock);
        			//funciona como es esperado
        			
//        			chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z-1), indexBlock);
        			//con y-1 sale en todas las caras pero es raro
//        			chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y-1, position.z), indexBlock);
//        		}
        		//**Con esto se buggea, cubre todo el edificio y 
//        		Block indexBlock = IndexCodeBlockFactory.generate();
//        		chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y-1, position.z), indexBlock);
        	}
        }
    }
    
    private boolean sliceHasCode(int[][] sliceBin){
    	if (sliceBin == null)
    		return false;
    	
    	return (sliceBin[0][0] == 1) ||
    			(sliceBin[0][1] == 1) ||
    			(sliceBin[1][0] == 1) ||
    			(sliceBin[1][1] == 1);
//    	for (int[] a : sliceBin)
//    		for (int i : a){
//    			if (i > 0)
//    				return true;
//    		}  			
//    	return false;
    }
}
