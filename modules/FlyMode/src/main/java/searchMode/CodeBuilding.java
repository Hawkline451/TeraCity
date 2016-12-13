package searchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;

public class CodeBuilding {
	
	private static CodeMapFactory codeMapFactory = new CodeMapFactory();
	private int height, width;
	Vector3i pos;
	
	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	
//	public CodeBuildingPositions getCodeBuilding(String className){
//		
//		CodeMap codeMap = CoreRegistry.get(CodeMap.class);
//		Set<MapObject> mapObjects = codeMap.getPosMapObjects();
//		  
//	    for(MapObject object : mapObjects){
//	    	if(object.containsClass(className)){
//	    		DrawableCodeSearchVisitor visitor = new DrawableCodeSearchVisitor(className);
//	    		object.getObject().accept(visitor);
//		  
//	    		while(true){
//	    			if(visitor.resultReady()){
//	    				Vector3i pos = visitor.getPosition();
//	    				int width = visitor.getWidth();
//	    				CodeBuildingPositions building = new CodeBuildingPositions(pos, width, object);
//	    				return building;
//		        }
//		      }
//		    }
//	    	
//	}

	/**
	 * @param origin position returned form a visitor
	 */
	public CodeBuilding(Vector3i origin, int w, MapObject object) {
		pos = origin;
		pos.setY(pos.getY()+10); //TODO Why +10?
		height = origin.getY();
		width = w;	
	}
	
    public Vector3i[][] getRoofPos(){
		Vector3i[][] positions = new Vector3i[width][width];
    	for(int x = 0; x < width; x++)
    		for(int z = 0; z < width; z++){
    			positions[x][z] = new Vector3i(pos.getX() + x, pos.getY(), pos.getZ() + z);
//    			currentPos = new Vector3i(position.getX() + x,position.getY() + 10, position.getZ() + z);
    		}
    	return positions;
    }
    
    
    /**
     * @return A list with the matrices of each faces
     */
    public List<Vector3i[][]> getFacesPos(){
    	List<Vector3i[][]> faces = new ArrayList<Vector3i[][]>();
    	faces.add(getSouthFacePos());
    	faces.add(getNorthFacePos());
    	faces.add(getWestFacePos());
    	faces.add(getEastFacePos());
    	return faces;
    }
    
    public Vector3i[][] getSouthFacePos(){
		Vector3i[][] positions = new Vector3i[height][width-2];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width-2; j++){
				positions[i][j] = new Vector3i(pos.getX() + j + 1, pos.getY() - i, pos.getZ() - 1);
			}
		}
    	return positions;
    }
    
    public Vector3i[][] getNorthFacePos(){
		Vector3i[][] positions = new Vector3i[height][width-2];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width-2; j++){
				positions[i][j] = new Vector3i(pos.getX() + j + 1, pos.getY() - i, pos.getZ() + width);
			}
		}
    	return positions;
    }
    
    public Vector3i[][] getWestFacePos(){
		Vector3i[][] positions = new Vector3i[height][width-2];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width-2; j++){
				positions[i][j] = new Vector3i(pos.getX() + width, pos.getY() - i, pos.getZ() + j + 1);
			}
		}
    	return positions;
    }
    
    public Vector3i[][] getEastFacePos(){
		Vector3i[][] positions = new Vector3i[height][width-2];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width-2; j++){
				positions[i][j] = new Vector3i(pos.getX() - 1, pos.getY() - i, pos.getZ() + j + 1);
			}
		}
    	return positions;
    }
    
    

}
