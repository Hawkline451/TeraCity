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
import org.terasology.rendering.nui.widgets.UIText;

public class CheckStyleMenuScreen extends CoreScreenLayer {

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
        
        UIText maxValue = find("maxValue", UIText.class);
        UIText pathProject = find("pathProject", UIText.class);
        
        WidgetUtil.trySubscribe(this, "ciclomatica", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	executeCommad(maxValue, pathProject, "-c");
            }
        });
        WidgetUtil.trySubscribe(this, "booleana", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	executeCommad(maxValue, pathProject,  "-b");
            }
        });
        WidgetUtil.trySubscribe(this, "fanOut", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	executeCommad(maxValue, pathProject,  "-f");
            }
        });
        WidgetUtil.trySubscribe(this, "nPath", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	executeCommad(maxValue, pathProject,  "-n");
            }
        });
        WidgetUtil.trySubscribe(this, "dataAbstractionCoupling", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	executeCommad(maxValue, pathProject,  "-d");
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
    
    private void executeCommad(UIText maxValueWindow, UIText pathWindow, String metric) {
    	ConsoleCommand ca = console.getCommand(new Name("cstyle"));
    	String maxValue = maxValueWindow.getText();
    	String path = pathWindow.getText();
    	ArrayList<String> params = new ArrayList<String>();
    	if (path.equals("")) params.add("modules/CheckStyle/Project/In");
    	else params.add(path);
    	if (metric.equals("-c")) {
    		params.add("-c");
        	params.add("10");
    	} else if (metric.equals("-b")) {
    		params.add("-b");
        	params.add("3");
    	}else if (metric.equals("-f")) {
        	params.add("-f");
        	params.add("20");
    	}else if (metric.equals("-n")) { 
        	params.add("-n");
        	params.add("200");
    	} else {
    		params.add("-d");
        	params.add("7");
    	}
    	if (!maxValue.equals("")) {
    		params.set(2, maxValue);
    	}
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
