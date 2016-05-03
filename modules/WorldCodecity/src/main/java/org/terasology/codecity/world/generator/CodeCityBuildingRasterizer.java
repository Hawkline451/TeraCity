package org.terasology.codecity.world.generator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;

import org.terasology.asset.Asset;
import org.terasology.asset.AssetData;
import org.terasology.asset.AssetManager;
import org.terasology.asset.AssetManagerImpl;
import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.codecity.world.facet.CodeCityFacet;
import org.terasology.codecity.world.map.ReducedViewBlockFactory;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.math.ChunkMath;
import org.terasology.math.Side;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockPart;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.AbstractBlockFamily;
import org.terasology.world.block.family.SymmetricFamily;
import org.terasology.world.block.internal.BlockManagerImpl;
import org.terasology.world.block.loader.TileData;
import org.terasology.world.block.loader.WorldAtlas;
import org.terasology.world.block.loader.WorldAtlasImpl;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;

/**
 * Rasterizes buildings using the information provided by CodeCityFacet
 */
public class CodeCityBuildingRasterizer implements WorldRasterizer {
    private Block block;
    private int counter;

    @Override
    public void initialize() {
        block = CoreRegistry.get(BlockManager.class).getBlock("core:stone");
    }

    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
    	int prev = -1;
        CodeCityFacet codeCityFacet = chunkRegion
                .getFacet(CodeCityFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {
        	if(codeCityFacet.containsBlock(position)){
        		int[] ll = codeCityFacet.getBlockType(position.x, position.y, position.z).getObject().getLineLength();
        		if (ll != null) {
        			if (prev == -1) {
        				prev = position.y;
        			}
        			else {
        				prev = Math.min(prev, position.y);
        			}
        		}
        		
        		if (counter == 0){
        			BlockManager bm = CoreRegistry.get(BlockManager.class);
        			AssetManager am = CoreRegistry.get(AssetManager.class);
        			
        			
        			Block testB = bm.getBlock("worldcodecity:test");
        			chunk.setBlock(ChunkMath.calcBlockPos(26, 12, 13), testB);
        			
        			
//        			AssetUri uri = new AssetUri("blocktile:worldcodecity:test");
        			SymmetricFamily abs = new SymmetricFamily(new BlockUri("worldcodecity","testFam"),new Block());
        			
        			AssetUri templateBlockDef = new AssetUri(AssetType.BLOCK_DEFINITION,"worldcodecity","test");
        			AssetUri familyUri = templateBlockDef;
        			Map<BlockPart, AssetUri> tileUris = new HashMap<BlockPart, AssetUri>(); 
        			

        			
        			
        			System.out.println("ITS ALIVE !!!!!!!!!!!!!!!!!!");


        			TileData td = ((AssetManagerImpl)am).parcheHorrendo("/home/andres/workspace/proyectoTeracity/rojo.png");
        			AssetUri newUri =  ((AssetManagerImpl)am).generateDynamicAsset(AssetType.BLOCK_TILE, td,"Textura");
        			
        			
//        			tileUris.put(BlockPart.BACK,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
//        			tileUris.put(BlockPart.TOP,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
//        			tileUris.put(BlockPart.LEFT,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
//        			tileUris.put(BlockPart.RIGHT,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
//        			tileUris.put(BlockPart.FRONT,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
//        			tileUris.put(BlockPart.BOTTOM,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
//        			tileUris.put(BlockPart.CENTER,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
        			
        			tileUris.put(BlockPart.BACK,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
        			tileUris.put(BlockPart.TOP,new AssetUri(AssetType.BLOCK_TILE,newUri.toSimpleString()));
        			tileUris.put(BlockPart.LEFT,new AssetUri(AssetType.BLOCK_TILE,"worldcodecity:col1"));
        			tileUris.put(BlockPart.RIGHT,new AssetUri(AssetType.BLOCK_TILE,newUri.toSimpleString()));
        			tileUris.put(BlockPart.FRONT,new AssetUri(AssetType.BLOCK_TILE,newUri.toSimpleString()));
        			tileUris.put(BlockPart.BOTTOM,new AssetUri(AssetType.BLOCK_TILE,newUri.toSimpleString()));
        			tileUris.put(BlockPart.CENTER,new AssetUri(AssetType.BLOCK_TILE,newUri.toSimpleString()));

        			
        			BlockUri franki = bm.createBlockFamily(templateBlockDef, familyUri, tileUris).getURI();
        			Block testLive = bm.getBlock(franki);
        			chunk.setBlock(ChunkMath.calcBlockPos(26, 15, 13), testLive);
        			
        			
        			        			
        			counter++;
        		}
        		
        		block = ReducedViewBlockFactory.generate(ll, position.y-prev);
        	    chunk.setBlock(ChunkMath.calcBlockPos(position.x, position.y, position.z), block);
        	}
        }
    }
}
