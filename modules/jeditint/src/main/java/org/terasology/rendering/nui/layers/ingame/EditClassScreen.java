package org.terasology.rendering.nui.layers.ingame;

import java.io.File;
import java.io.IOException;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.engine.GameEngine;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.widgets.UIText;
import org.terasology.utilities.jedit.EditClass;
import org.terasology.utilities.jedit.JeditManager;
import org.terasology.utilities.jedit.ClassPathVisitor;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.registry.In;

import org.terasology.rendering.FontColor;
import org.terasology.rendering.nui.Color;

/**
 * @author Francisco Pulgar Romero
 */

public class EditClassScreen extends CoreScreenLayer{
	
	@In
	private CameraTargetSystem cameraTarget;	
	private String path;
	private UIText textclass;
	private UILabel pathClass;
	
	
	@Override
	public void onOpened() { 
		
		path = JeditManager.getPath(cameraTarget);
		
		try{
			File f = new File(path);
			if(f.exists() && !f.isDirectory()) { 
				pathClass = find("subtitle",UILabel.class);
				pathClass.setText(path);
				
				textclass = find("textclass", UIText.class);
				
				String contentClass = "";
				try {
					contentClass = EditClass.readFileAsString(path);
					Color green = new Color(0xCCFF99);
					String textWithColoredComments = contentClass.replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)", FontColor.getColored("$0", green));
					textclass.setText(textWithColoredComments);
					
					initialise();
					System.out.println("Read "+path);
				} catch (IOException e) {
					//e.printStackTrace();
					System.out.println("No File read!");
				}
			}else if(f.isDirectory()){
				System.out.println(path+" is a directory not a file!");
			}else{
				System.out.println("No ClassPath found!");
			}
		 }
		 catch(Exception e1) {
			 System.out.println("ERROR READ!");
		 }
	}	
	
	@Override
    public void initialise() {
		
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
            	String editContentWithoutColor =FontColor.stripColor(editContent);
            	try {
					EditClass.writeFile(pathClass.getText(), editContentWithoutColor);
				} catch (IOException e) {
					e.printStackTrace();
				}
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
