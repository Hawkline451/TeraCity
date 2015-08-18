package org.terasology.logic.console.commands;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.map.NullMapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.world.WorldRenderer;
import org.terasology.utilities.jedit.JeditManager;


@RegisterSystem
public class JeditCommand  extends BaseComponentSystem {
	 @In
	 private Console console;   
	 @In
	 private CameraTargetSystem cameraTarget;
	 CodeScale scale = new SquareRootCodeScale();
     CodeMapFactory factory = new CodeMapFactory(scale);
	 
	 @Command( shortDescription = "Open jedit", helpText = "Open jedit in the class of the selected structure" )
     public String jedit(@CommandParam("Class") String className) {
		return JeditManager.openJedit(className);
	 }
	 @Command( shortDescription = "Get Path", helpText = "Get path of an object" )
     public String getPath() {
		WorldRenderer renderer = CoreRegistry.get(WorldRenderer.class);
		CodeMap Map = CoreRegistry.get(CodeMap.class);
		
	    Camera camera= renderer.getActiveCamera();	
     	Vector3f objectPos = camera.getPosition();
	    Vector3f offset = camera.getViewingDirection();
	    offset.scale(3);
	    objectPos.add(offset);
	    
	    int x = cameraTarget.getTargetBlockPosition().getX();
        int y = cameraTarget.getTargetBlockPosition().getZ();
        int z= cameraTarget.getTargetBlockPosition().getY();
        
	    int base = 9;
	    MapObject obj = getMapObject(Map, Vector2i.zero(),base,x,y,z);
	    CodeRepresentation code = obj.getObject().getBase();
	    
       
        
	   return code.getPath();
	 }
	 private MapObject getMapObject(CodeMap map, Vector2i offset, int bottom, int x1, int y1, int z1) {
	        for (MapObject obj : map.getMapObjects()) {
	            int x = obj.getPositionX() + offset.getX();
	            int y = obj.getPositionZ() + offset.getY();
	            System.out.println(x+","+y+","+bottom);
	            int top = obj.getHeight(scale, factory) + bottom;
	            if(x1==x && y1==y && z1>bottom && z1<=top) return obj;
	            if (obj.isOrigin()){
	                MapObject mo = getMapObject(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), top, x1, y1, z1);
	                if (mo!=null) return mo;
	            }
	        }
	        return null;
	 }
}
