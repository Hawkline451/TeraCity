package org.terasology.codecity.world.map;

import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.structure.metric.CodeMetricManager;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.CodeScaleManager;
import org.terasology.registry.CoreRegistry;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3i;

/**
 *
 * Code represented has a 3d building.
 * Building has the
 *
 *
 */
public class CodeBuilding {


	//Code this building represents
	protected DrawableCode code;
	//Position of the building in the map
	protected int origin_x, origin_z;
	//Height of the building and dimension of the n x n base
	protected int height, baseSize;

	//Building Structure
	//Faces
	protected BuildingFace north, south, east, west;
	//Roof
	protected BuildingRoof roof;

	public CodeBuilding(DrawableCode content, int x0, int y0){
		this(content, x0, y0, new CodeMapFactory() );
	}


	public CodeBuilding(DrawableCode content, int x0, int y0,
			CodeMapFactory factory){

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


    public void setOrigin(int x0, int z0){
    	origin_x = x0;
    	origin_z = z0;
    }

    public Vector2i getOrigin(){
    	return new Vector2i(origin_x, origin_z);
    }

    public int getHeight(){
    	return this.height;
    }

    private Vector3i[][] positions = null;
    public Vector3i[][] getRoofCoor(){
    	if (positions == null){
    		positions = new Vector3i[baseSize][baseSize];
	    	for(int x = 0; x < baseSize; x++)
	    		for(int z = 0; z < baseSize; z++){
	    			positions[x][z] = new Vector3i(origin_x + x, height, origin_z + z);
	    		}
    	}
    	return positions;
    }

    public boolean containsClass(String className){
    	return code.containsClass(className);
    }

    public DrawableCode getCode(){
    	return this.code;
    }

    public String getClassName(){
    	return code.getBase().getName();
    }

//    public List<Vector3i> getNorthFaceCoor(){
//
//    	//Face has dimensions height-1 (minus roof) and baseSize-2 (columns)
//    	Vector3i[][] positions = new Vector3i[height-1][baseSize-2];
//
//    	return positions;
//    }
//
//    public List<Vector3i> getSouthFaceCoor(){
//    	List<Vector3i> positions = new ArrayList<Vector3i>();
//
//    	return positions;
//    }
//
//    public List<Vector3i> getWestFaceCoor(){
//    	List<Vector3i> positions = new ArrayList<Vector3i>();
//
//    	return positions;
//    }
//
//    public List<Vector3i> getEastFaceCoor(){
//    	List<Vector3i> positions = new ArrayList<Vector3i>();
//
//    	return positions;
//    }
//


}
