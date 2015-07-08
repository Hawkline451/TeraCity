package org.terasology.codecity.world.generator;

import org.terasology.codecity.world.facet.CodeCityFacet;
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
    private Block stone;	

    @Override
    public void initialize() {
        stone = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        CodeCityFacet codeCityFacet = chunkRegion
                .getFacet(CodeCityFacet.class);
        
        for (Vector3i position : chunkRegion.getRegion()) {
        	if(codeCityFacet.containsBlock(position)){
        	    chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z), stone);
        	}
        }
    }
}
