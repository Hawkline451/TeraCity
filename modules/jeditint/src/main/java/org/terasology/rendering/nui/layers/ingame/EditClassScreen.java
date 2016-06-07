package org.terasology.rendering.nui.layers.ingame;

import java.io.IOException;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.engine.GameEngine;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.ShaderManager;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.widgets.UIText;
import org.terasology.rendering.nui.widgets.UITextEntry;
import org.terasology.utilities.jedit.EditClass;
import org.terasology.utilities.jedit.JeditManager;
import org.terasology.utilities.jedit.ClassPathVisitor;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.network.ClientComponent;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.codecity.world.structure.CodeRepresentation;

/**
 * @author Francisco Pulgar Romero
 */

public class EditClassScreen extends CoreScreenLayer{
	
	@In
	private CameraTargetSystem cameraTarget;	
	
	private UIText textclass;
	private UILabel pathClass;
	
	@Override
    public void initialise() {
		
		
		
		CodeRepresentation code = CodeRepresentation.getCodeRepresentation(cameraTarget);
		ClassPathVisitor visitor = new ClassPathVisitor();
		code.accept(visitor);
		
		String path = JeditManager.returnPath(visitor);
		
		pathClass = find("subtitle",UILabel.class);
		pathClass.setText(path);
		
		textclass = find("textclass", UIText.class);
		
		String contentClass = "";
		try {
			contentClass = EditClass.readFileAsString(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		textclass.setText(contentClass);
    
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                getManager().closeScreen(EditClassScreen.this);
            }
        });
        
        /**
         * Send code in 
         */ 
        WidgetUtil.trySubscribe(this, "save", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	String editContent = textclass.getText();
            	//EditClass.writeFile(pathClass.getText(), editContent);
                getManager().closeScreen(EditClassScreen.this);
            }
        });        
        
        WidgetUtil.trySubscribe(this, "exit", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                CoreRegistry.get(GameEngine.class).shutdown();
            }
        });
    }
}
