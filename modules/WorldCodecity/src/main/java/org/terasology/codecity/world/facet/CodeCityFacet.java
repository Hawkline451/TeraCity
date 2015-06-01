package org.terasology.codecity.world.facet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.terasology.codecity.world.map.MapObject;
import org.terasology.math.Region3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.facets.base.BaseFieldFacet2D;

public class CodeCityFacet extends BaseFieldFacet2D {
    private HashMap<String, MapObject> blocks;
	private int base = 0;

    public CodeCityFacet(Region3i targetRegion, Border3D border, int base){
        super(targetRegion, border);
        this.base = base;
        this.blocks = new HashMap<String, MapObject>();
    }
    
    public int getBaseHeight(){
    	return this.base;
    }
    
    public void setMapObject(int x, int y, int z, MapObject block){
        blocks.put(encodePosition(x, y, z), block);
    }
    
    public MapObject getBlockType(int x, int y, int z) {
        String key = encodePosition(x, y, z);
            return blocks.get(key);
    }
    
    public boolean containsBlock(int x, int y, int z) {
        String key = encodePosition(x, y, z);
        return blocks.containsKey(key);
    }
    
    public boolean containsBlock(Vector3i vector){
        return containsBlock(vector.x, vector.y, vector.z);
    }
    
    private String encodePosition(int x, int y, int z){
        return x+","+y+","+z;
    }
    
    public List<Vector3i> getPositions(){
        List<Vector3i> list = new ArrayList<Vector3i>();
        for (String key : blocks.keySet()){
            list.add(decodePosition(key));
        }
        return list;
    }

    private Vector3i decodePosition(String key) {
        String[] indexs = key.split(",");
        return new Vector3i(Integer.parseInt(indexs[0]),
                Integer.parseInt(indexs[1]),
                Integer.parseInt(indexs[2]));
    }
}