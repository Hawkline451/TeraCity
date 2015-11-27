/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.terasology.rendering.nui.layers.ingame.coloring.FaceToPaint;
import org.terasology.rendering.nui.layers.mainMenu.inputSettings.InputSettingsScreen;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIDropdown;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.widgets.UIText;

import com.google.common.collect.Lists;

public class CheckStyleMenuScreen extends CoreScreenLayer {

    private static final AssetUri INPUT_SCREEN_URI = new AssetUri(AssetType.UI_ELEMENT, "engine:inputScreen");

    @In
    private Config config;
    
    @In
    private Console console;

    @SuppressWarnings("unchecked")
	@Override
    public void initialise() {
        CoreScreenLayer inputScreen = new InputSettingsScreen();
        inputScreen.setSkin(getSkin());
        UIData inputScreenData = new UIData(inputScreen);
        Assets.generateAsset(INPUT_SCREEN_URI, inputScreenData, UIElement.class);
        
        final UIText maxValue = find("maxValue", UIText.class);
        final UIText pathProject = find("pathProject", UIText.class);
        
        final UIDropdown<FaceToPaint> faceToPaint = find("faceToPaint", UIDropdown.class);
        if (faceToPaint != null) {
            faceToPaint.setOptions(Lists.newArrayList(FaceToPaint.ALL, FaceToPaint.NORTH, FaceToPaint.EAST, FaceToPaint.WEST, FaceToPaint.SOUTH));
        }
        
        // displays info to the user: warnings, errors, ...
        final UILabel infoField = find("infoField", UILabel.class);
        
        WidgetUtil.trySubscribe(this, "ciclomatica", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(maxValue, pathProject, "-c", face);
            }
        });
        WidgetUtil.trySubscribe(this, "booleana", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(maxValue, pathProject,  "-b", face);
            }
        });
        WidgetUtil.trySubscribe(this, "fanOut", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(maxValue, pathProject,  "-f", face);
            }
        });
        WidgetUtil.trySubscribe(this, "nPath", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(maxValue, pathProject,  "-n", face);
            }
        });
        WidgetUtil.trySubscribe(this, "dataAbstractionCoupling", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(maxValue, pathProject, "-d", face);
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
    
    private void executeCommand(UIText maxValueWindow, UIText pathWindow, String metric, FaceToPaint face) {
    	
    	// manage invalid face selections
    	final UILabel infoField = find("infoField", UILabel.class);
    	if (face == null) {
    		infoField.setText("waning: please choose a face to paint!");
    		return;
    	}
    	infoField.setText("");
    	
    	// send paint command
    	ConsoleCommand ca = console.getCommand(new Name("paintWithCheckStyle"));
    	String maxValue = maxValueWindow.getText();
    	String path = pathWindow.getText();
    	ArrayList<String> params = new ArrayList<String>();
    	if (path.equals("")) params.add("default");
    	else params.add(path);
    	if (metric.equals("-c")) {
    		params.add("-c");
        	params.add("10");
    	} else if (metric.equals("-b")) {
    		params.add("-b");
        	params.add("3");
    	} else if (metric.equals("-f")) {
        	params.add("-f");
        	params.add("20");
    	} else if (metric.equals("-n")) { 
        	params.add("-n");
        	params.add("200");
    	} else {
    		params.add("-d");
        	params.add("7");
    	}
    	if (!maxValue.equals("")) {
    		params.set(2, maxValue);
    	}
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
