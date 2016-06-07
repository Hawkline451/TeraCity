package org.terasology.rendering.nui.layers.ingame;

import org.terasology.engine.GameEngine;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.widgets.UIText;
import org.terasology.rendering.nui.widgets.UITextEntry;
//import org.terasology.utilities.jedit.EditClass;

/**
 * @author Francisco Pulgar Romero
 */

public class EditClassScreen extends CoreScreenLayer{
	
	private UITextEntry textclass;
	private UILabel pathClass;
	
	@Override
    public void initialise() {
		
		pathClass = find("subtitle",UILabel.class);
		pathClass.setText("ruta de la clase");
		
		textclass = find("textclass", UITextEntry.class);
		textclass.setText("contenido del archivo");
    
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
