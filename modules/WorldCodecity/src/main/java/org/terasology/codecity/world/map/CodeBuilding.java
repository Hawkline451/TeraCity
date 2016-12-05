package org.terasology.codecity.world.map;

import org.terasology.codecity.world.structure.metric.CodeMetricManager;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.CodeScaleManager;
import org.terasology.registry.CoreRegistry;

/**
 * Code represented has a 3d building
 *   
 */
public class CodeBuilding {
	
	
	//Code this building represents
	protected DrawableCode code;
	//Position of the building in the map
	protected int x, y;
	//Height of the building and dimension of the n x n base
	protected int height, baseSize;
	
	//Building Structure
	//Faces 
	protected BuildingFace north, south, east, west;
	//Roof 
	protected BuildingRoof roof;
	
	
	public CodeBuilding(DrawableCode content, CodeScale scale,
    CodeMapFactory factory, int x0, int y0){
		
		code = content;
		setOrigin(x0, y0);
		int buildingSize = content.getSize(factory);
		setBaseSize(buildingSize);
		//Set height
        height = code.getHeight(factory);
		

	}
	
	
    private void setHeight(int h){
    	height = h;
    }
    
    private void setBaseSize(int size){
    	baseSize = size;
    }
    
	
    public void setOrigin(int x0, int y0){
    	x = x0;
    	y = y0;
    }
    
    public void setRoof(BuildingRoof r){
    	
    }
    
    public void setNorthFace(BuildingFace face){
    	
    }
    
    public void setSouthFace(BuildingFace face){
    	
    }
    
    public void setWestFace(BuildingFace face){
    	
    }
    
    public void setEastFace(BuildingFace face){
    	
    }
    
	
	
}
