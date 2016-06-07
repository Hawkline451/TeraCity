package org.terasology.logic.jedit;

import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.ButtonState;
import org.terasology.input.binds.general.EditClassButton;
import org.terasology.input.binds.general.PauseButton;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.rendering.nui.ControlWidget;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.asset.UIData;
import org.terasology.rendering.nui.asset.UIElement;
import org.terasology.rendering.nui.layers.ingame.inventory.TransferItemCursor;
import org.terasology.utilities.jedit.JeditManager;

/**
 * Edit Class inside Minecraft
 * @author Francisco Pulgar
 */
@RegisterSystem
public class EditClassSystem extends BaseComponentSystem {
	
	 @In
	 private CameraTargetSystem cameraTarget;
	 
	 @In
	 private NUIManager nuiManager;
	 
	 @Override
	 
	 public void initialise() {
		 nuiManager.getHUD().addHUDElement("toolbar");
		 TransferItemCursor cursor = new TransferItemCursor();
		 UIElement cursorElement = Assets.generateAsset(new AssetUri(AssetType.UI_ELEMENT, "engine:transferItemCursor"), new UIData(cursor), UIElement.class);
		 nuiManager.addOverlay(cursorElement, ControlWidget.class);
	    }	 
	 
	 
	 /**
	  * edit Class inside of Minecraft of the TargetBlock MapObject		
	  * @param event
	  * @param entity
	  */
	 @ReceiveEvent(components = ClientComponent.class)
	 public void openEditClass(EditClassButton event, EntityRef entity) {
		 if (event.getState() == ButtonState.DOWN) {
			 JeditManager.editClassWhenPressed(cameraTarget, nuiManager);
	         event.consume();
	    } 
	 }
	 
}

