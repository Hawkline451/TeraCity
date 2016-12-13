package org.terasology.codecity.world.generator;

import java.util.Arrays;
import java.util.Collections;

import org.terasology.codecity.world.facet.CodeCityFacet;
import org.terasology.codecity.world.map.DrawableCode;
import org.terasology.codecity.world.map.DrawableCodeClass;

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
            		
            		// Generate the invisible block related to the block code if it has code
            		if (map.isIndexBlock()){
            			block = CoreRegistry.get(BlockManager.class).getBlock("core:transparentGreen");
            		}
            		else{
            		int[][] sliceBin = ReducedViewBlockFactory.recalcBinary(code,row,col);
            		block = ReducedViewBlockFactory.generate(sliceBin);
            		}
            		//HERE GOES THE NEW FACTORY THAT TRANSLATE BLOQUE TO THE CORRENT BLOCK
            		
//            		if (map.getObject() instanceof DrawableCodeClass){
//	            		MapObject.Facing facing = map.getFacing();
//	            		Block indexBlock = CoreRegistry.get(BlockManager.class).getBlock("core:Ice");
//	            		if (facing == MapObject.Facing.SOUTH){
//	            			chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z - 1), indexBlock);
//	            		}
//	            		else if (facing == MapObject.Facing.NORTH){
//	            			chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z + 1), indexBlock);
//	            		}
//	            		else if (facing == MapObject.Facing.WEST){
//	            			chunk.setBlock(ChunkMath.calcBlockPos(position.x - 1, position.y, position.z), indexBlock);
//	            		}
//	        			else if (facing == MapObject.Facing.EAST){
//	        				chunk.setBlock(ChunkMath.calcBlockPos(position.x + 1, position.y, position.z), indexBlock);
//	            		}
//            		}

            	    
        		}
        		else{ //Here block for borders which have map.getColumn() == -1
        			block = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
        		}
        		chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z), block);
        		
        	}
        }
    }
    
}
    