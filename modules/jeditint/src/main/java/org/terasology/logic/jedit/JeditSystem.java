package org.terasology.logic.jedit;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.input.binds.general.JeditButton;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.utilities.jedit.JeditManager;

/**
 * Open Jedit
 * @author Patricio Huepe
 */
@RegisterSystem
public class JeditSystem extends BaseComponentSystem {
	
	 @In
	 private CameraTargetSystem cameraTarget;
	
	 /**
	  * Open jEdit of the TargetBlock MapObject		
	  * @param event
	  * @param entity
	  */
	 @ReceiveEvent(components = ClientComponent.class)
	 public void openJedit(JeditButton event, EntityRef entity) {
		 if (event.getState() == ButtonState.DOWN) {
			 JeditManager.openJeditWhenPressed(cameraTarget);
	         event.consume();
	    }
	 }
}

