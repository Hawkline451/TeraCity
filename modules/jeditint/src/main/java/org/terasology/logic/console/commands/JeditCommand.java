package org.terasology.logic.console.commands;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.utilities.jedit.JeditManager;


@RegisterSystem
public class JeditCommand  extends BaseComponentSystem {
	 JeditManager manager = new JeditManager();
	 
	 @In
	 private CameraTargetSystem cameraTarget;
	 
	 @Command( shortDescription = "Open jedit", helpText = "Open jedit in the class of the selected structure" )
     public String jedit(@CommandParam("Class") String className) {
		return manager.openClass(className);
	 }
	 @Command( shortDescription = "Get Path", helpText = "Get path of an object" )
     public String getPath() {
		CodeMap map = CoreRegistry.get(CodeMap.class);
	    return manager.getPath(cameraTarget,map);
	 }
}
