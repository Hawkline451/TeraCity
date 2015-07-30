package org.terasology.logic.jedit;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.input.binds.general.JeditButton;
import org.terasology.network.ClientComponent;
import org.terasology.utilities.jedit.JeditManager;

/**
 * Open Jedit
 * @author Patricio Huepe
 */
@RegisterSystem
public class JeditSystem extends BaseComponentSystem {
	 @ReceiveEvent(components = ClientComponent.class)
	 public void onToggleChat(JeditButton event, EntityRef entity) {
		 if (event.getState() == ButtonState.DOWN) {
			 JeditManager.openJedit("test.java");
	         event.consume();
	    }
	 }
}