package org.terasology.logic.console.commands;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.world.WorldRenderer;
import org.terasology.utilities.jedit.JeditManager;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;


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
	    
	    MapObject object = Map.getMapObject((int)objectPos.x, (int)objectPos.z);
	    CodeRepresentation code = object.getObject().getBase();
	    
		return code.getPath();
	 }
}
