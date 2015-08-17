package org.terasology.logic.console.commands;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.world.WorldRenderer;
import org.terasology.utilities.jedit.JeditManager;
import org.terasology.world.WorldProvider;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;


@RegisterSystem
public class JeditCommand  extends BaseComponentSystem {
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
	    
	    int x = (int)objectPos.x;
        int y = (int)objectPos.z;
        int z = (int)objectPos.y;
	    
	    MapObject obj = Map.getMapObject(x, y);

        CodeScale scale = new SquareRootCodeScale();
        CodeMapFactory factory = new CodeMapFactory(scale);
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        
        int base = 9;
        int totalHeight = base + obj.getHeight(scale, factory);
       
        while (totalHeight<z && obj!=null){
        	Map = obj.getObject().getSubmap(scale, factory);
        	obj = Map.getMapObject(x, y);
        	totalHeight += obj.getHeight(scale, factory);
        }
	   
	   CodeRepresentation code = obj.getObject().getBase();
	   return code.getPath() +" "+totalHeight+" "+z+" "+(int)9.5;
	 }
}
