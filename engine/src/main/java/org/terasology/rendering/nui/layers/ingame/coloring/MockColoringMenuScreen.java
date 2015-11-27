package org.terasology.rendering.nui.layers.ingame.coloring;

import java.util.ArrayList;

import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.config.Config;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.ConsoleCommand;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.naming.Name;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.asset.UIData;
import org.terasology.rendering.nui.asset.UIElement;
import org.terasology.rendering.nui.layers.mainMenu.inputSettings.InputSettingsScreen;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIDropdown;
import org.terasology.rendering.nui.widgets.UILabel;
import com.google.common.collect.Lists;


public class MockColoringMenuScreen extends CoreScreenLayer{
	 private static final AssetUri INPUT_SCREEN_URI = new AssetUri(AssetType.UI_ELEMENT, "engine:inputScreen");

	    @In
	    private Config config;
	    
	    @In
	    private Console console;

	    @Override
	    @SuppressWarnings("unchecked")
	    public void initialise() {
	        CoreScreenLayer inputScreen = new InputSettingsScreen();
	        inputScreen.setSkin(getSkin());
	        UIData inputScreenData = new UIData(inputScreen);
	        Assets.generateAsset(INPUT_SCREEN_URI, inputScreenData, UIElement.class);
	        
	        final UIDropdown<FaceToPaint> faceToPaint = find("faceToPaint", UIDropdown.class);
	        if (faceToPaint != null) {
	            faceToPaint.setOptions(Lists.newArrayList(FaceToPaint.ALL, FaceToPaint.NORTH, FaceToPaint.EAST, FaceToPaint.WEST, FaceToPaint.SOUTH));
	        }
	        
	        // displays info to the user: warnings, errors, ...
	        final UILabel infoField = find("infoField", UILabel.class);
	        
	        
	        WidgetUtil.trySubscribe(this, "randomMetric", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            		
	            	FaceToPaint face = faceToPaint.getSelection();
	            	executeCommand("random", face);
	            }
	        });
	        
	        WidgetUtil.trySubscribe(this, "goodMetric", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	
	            	FaceToPaint face = faceToPaint.getSelection();
	            	executeCommand("good", face);
	            }
	        });
	        
	        
	        
	        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget button) {
	            	infoField.setText("");
	            	config.save();
	            	getManager().popScreen();
	            }
	        });
	    }
	    
	    
	    private void executeCommand(String metric, FaceToPaint face) {
	    	
	    	// manage invalid face selections
	    	final UILabel infoField = find("infoField", UILabel.class);
        	if (face == null) {
        		infoField.setText("waning: please choose a face to paint!");
        		return;
        	}
        	infoField.setText("");
	    	
        	// send paint command
	    	ConsoleCommand ca = console.getCommand(new Name("paintWithMockColoring"));	
	    	ArrayList<String> params = new ArrayList<String>();
	    	params.add(metric);
	    	params.add(face.toString());
	    	EntityRef e = null;
	    	try {
	    		ca.execute(params, e);
	    	} catch (CommandExecutionException e1) {
	    	}
	    }
	    
	    @Override
	    public boolean isLowerLayerVisible() {
	    	return false;
	    }
}
