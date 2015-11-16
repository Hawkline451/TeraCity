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
import org.terasology.rendering.nui.widgets.UIDropdown;
import org.terasology.rendering.nui.widgets.UILabel;

import com.google.common.collect.Lists;

public class PMDMenuScreen extends CoreScreenLayer {

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
        
        
        UIDropdown<FaceToPaint> faceToPaint = find("faceToPaint", UIDropdown.class);
        if (faceToPaint != null) {
            faceToPaint.setOptions(Lists.newArrayList(FaceToPaint.ALL, FaceToPaint.NORTH, FaceToPaint.EAST, FaceToPaint.WEST, FaceToPaint.SOUTH));
        }
        
        // displays info to the user: warnings, errors, ...
        final UILabel infoField = find("infoField", UILabel.class);
        
        WidgetUtil.trySubscribe(this, "comments", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("comments", face);
            }
        });
        WidgetUtil.trySubscribe(this, "commentrequired", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("commentrequired", face);
            }
        });
        WidgetUtil.trySubscribe(this, "commentsize", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("commentsize", face);
            }
        });
        WidgetUtil.trySubscribe(this, "commentcontent", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("commentcontent", face);
            }
        });
        WidgetUtil.trySubscribe(this, "codesize", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("codesize", face);
            }
        });
        WidgetUtil.trySubscribe(this, "npathcomplexity", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("npathcomplexity", face);
            }
        });
        WidgetUtil.trySubscribe(this, "cyclomaticcomplexity", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("cyclomaticcomplexity", face);
            }
        });
        WidgetUtil.trySubscribe(this, "toomanymethods", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("toomanymethods", face);
            }
        });
        WidgetUtil.trySubscribe(this, "coupling", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("coupling", face);
            }
        });
        WidgetUtil.trySubscribe(this, "couplingbetweenobjects", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("couplingbetweenobjects", face);
            }
        });
        WidgetUtil.trySubscribe(this, "excessiveimports", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("excessiveimports", face);
            }
        });
        WidgetUtil.trySubscribe(this, "lawofdemeter", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand("lawofdemeter", face);
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
    	ConsoleCommand ca = console.getCommand(new Name("paintWithPMD"));
    	ArrayList<String> params = new ArrayList<String>();
    	params.add(metric);
    	params.add(face.toString());
    	try {
			ca.execute(params, EntityRef.NULL);
		} catch (CommandExecutionException e1) {
		}
    }

    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }
}
