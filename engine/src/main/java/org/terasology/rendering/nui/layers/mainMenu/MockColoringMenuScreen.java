package org.terasology.rendering.nui.layers.mainMenu;

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


public class MockColoringMenuScreen extends CoreScreenLayer{
	 private static final AssetUri INPUT_SCREEN_URI = new AssetUri(AssetType.UI_ELEMENT, "engine:inputScreen");

	    @In
	    private Config config;
	    
	    @In
	    private Console console;

	    @Override
	    public void initialise() {
	        CoreScreenLayer inputScreen = new InputSettingsScreen();
	        inputScreen.setSkin(getSkin());
	        UIData inputScreenData = new UIData(inputScreen);
	        Assets.generateAsset(INPUT_SCREEN_URI, inputScreenData, UIElement.class);
	        
	        
	        WidgetUtil.trySubscribe(this, "randomMetric", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	executeCommand("random");
	            }
	        });
	        WidgetUtil.trySubscribe(this, "goodMetric", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	executeCommand("good");
	            }
	        });
	       
	        
	    
	        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget button) {
	                config.save();
	                getManager().popScreen();
	            }
	        });
	    }
	    
	    private void executeCommand(String metric) {
	    	ConsoleCommand ca = console.getCommand(new Name("paintWithMockColoring"));	
	    	ArrayList<String> params = new ArrayList<String>();
			params.add(metric);
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
