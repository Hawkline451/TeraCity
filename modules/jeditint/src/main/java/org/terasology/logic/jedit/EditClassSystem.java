package org.terasology.logic.jedit;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.input.binds.general.EditClassButton;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.utilities.jedit.JeditManager;

/**
 * Edit Class inside Minecraft
 * @author Francisco Pulgar
 */
@RegisterSystem
public class EditClassSystem extends BaseComponentSystem {
	
	 @In
	 private CameraTargetSystem cameraTarget;
	
	 /**
	  * edit Class inside of Minecraft of the TargetBlock MapObject		
	  * @param event
	  * @param entity
	  */
	 @ReceiveEvent(components = ClientComponent.class)
	 public void openJedit(EditClassButton event, EntityRef entity) {
		 if (event.getState() == ButtonState.DOWN) {
			 JeditManager.editClassWhenPressed(cameraTarget);
	         event.consume();
	    }
	 }
}

