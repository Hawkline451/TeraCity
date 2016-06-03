package org.terasology.codecity.world.structure;

import java.io.Serializable;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.IMapObject;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.HalfLinearCodeScale;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.math.Vector2i;
import org.terasology.registry.CoreRegistry;

/**
 * This class show the size of a portion of the code.
 */
public abstract class CodeRepresentation implements Serializable {
    private static final long serialVersionUID = -6189951858152671617L;
    private String name;
    private String path;
    private String github;
    
    /**
     * @param name Name of the represented code
     * @param github Github link to the document
     */
    public CodeRepresentation(String name, String path, String github) {
        this.name = name;
        this.path = path;
        this.github = github;
    }
    
    /**
     * Create CodeRepresentation without parameters
     */
    public CodeRepresentation(){
    }
    /**
     * @return Github link to the document
     */
    public String getGithubDir() {
        return github;
    }
    
    public String getPath() {
        return path;
    }
    
    /**
     * @return Name of the code represented
     */
    public String getName() {
        return name;
    }
    
    /**
     * Accept a visitor in the class
     * @param visitor
     */
    public abstract void accept(CodeVisitor visitor);
    
    public boolean validCode(){
    	return true;
    }
    
    public abstract int size();
    
    /**
     * Obtains the MapObject in the codemap map with coordenates (x1,y1,z1) 
     * @param map map of objects that represent the proyect code.
     * @param offset (dx,dy) from (x1, y1) to find the object.
     * @param bottom coordinate z of the floor.
     * @param x1 coordinate x to find.
     * @param y1 coordinate y to find.
     * @param z1 coordinate z to find.
     * @return MapObject in the position (x1,y1,z1)
     */
    private static IMapObject getMapObject(CodeMap map, Vector2i offset, int bottom, int x1, int y1,
        int z1) {
      CodeScale scale = new HalfLinearCodeScale();
      CodeMapFactory factory = new CodeMapFactory(scale);
  
      for (MapObject obj : map.getMapObjects()) {
        int x = obj.getPositionX() + offset.getX();
        int y = obj.getPositionZ() + offset.getY();
        int top = obj.getHeight(factory) + bottom;
  
        if (x1 == x && y1 == y && z1 > bottom && z1 <= top)
          return obj;
  
        if (obj.isOrigin()) {
          IMapObject mo = getMapObject(obj.getObject().getSubmap(scale, factory),
              new Vector2i(x + 1, y + 1), top, x1, y1, z1);
          if (mo.getHeight(factory) != 0)
            return mo;
        }
      }
      return new NullMapObject();
    }
    
    /**
     * Get CodeRepresentation of the mapObject targeted by the camera.
     * @return CodeRepresentation of the mapObject targeted by the camera.
     */
    public static CodeRepresentation getCodeRepresentation(CameraTargetSystem cameraTarget) {
      
      int x = cameraTarget.getTargetBlockPosition().getX();
      int y = cameraTarget.getTargetBlockPosition().getZ();
      int z = cameraTarget.getTargetBlockPosition().getY();
      int base = 9;
      
      CodeMap map = CoreRegistry.get(CodeMap.class);
      IMapObject obj = getMapObject(map, Vector2i.zero(), base, x, y, z);
      CodeRepresentation code = obj.getObject().getBase();
      return code;
    }
}
