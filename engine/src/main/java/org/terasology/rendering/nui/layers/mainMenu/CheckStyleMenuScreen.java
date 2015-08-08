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
        
        WidgetUtil.trySubscribe(this, "ciclomatica", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	ConsoleCommand ca = console.getCommand(new Name("cstyle"));
            	ArrayList<String> params = new ArrayList<String>();
            	params.add("modules/CheckStyle/Project/In"); 
            	params.add("-c");
            	params.add("10");
            	EntityRef e = null;
            	try {
					ca.execute(params, e);
					System.out.println("\n C: \n");
				} catch (CommandExecutionException e1) {
					System.out.println("\nNo se pudo realizar el análisis\n");
				}
            }
        });
        WidgetUtil.trySubscribe(this, "booleana", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	ConsoleCommand ca = console.getCommand(new Name("cstyle"));
            	ArrayList<String> params = new ArrayList<String>();
            	params.add("modules/CheckStyle/Project/In"); 
            	params.add("-b");
            	params.add("3");
            	EntityRef e = null;
            	try {
					ca.execute(params, e);
					System.out.println("\n C: \n");
				} catch (CommandExecutionException e1) {
					System.out.println("\nNo se pudo realizar el análisis\n");
				}
            }
        });
        WidgetUtil.trySubscribe(this, "fanOut", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	ConsoleCommand ca = console.getCommand(new Name("cstyle"));
            	ArrayList<String> params = new ArrayList<String>();
            	params.add("modules/CheckStyle/Project/In"); 
            	params.add("-f");
            	params.add("10");
            	EntityRef e = null;
            	try {
					ca.execute(params, e);
					System.out.println("\n C: \n");
				} catch (CommandExecutionException e1) {
					System.out.println("\nNo se pudo realizar el análisis\n");
				}
            }
        });
        WidgetUtil.trySubscribe(this, "nPath", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	ConsoleCommand ca = console.getCommand(new Name("cstyle"));
            	ArrayList<String> params = new ArrayList<String>();
            	params.add("modules/CheckStyle/Project/In"); 
            	params.add("-n");
            	params.add("1000");
            	EntityRef e = null;
            	try {
					ca.execute(params, e);
					System.out.println("\n C: \n");
				} catch (CommandExecutionException e1) {
					System.out.println("\nNo se pudo realizar el análisis\n");
				}
            }
        });
        WidgetUtil.trySubscribe(this, "dataAbstractionCoupling", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	ConsoleCommand ca = console.getCommand(new Name("cstyle"));
            	ArrayList<String> params = new ArrayList<String>();
            	params.add("modules/CheckStyle/Project/In"); 
            	params.add("-d");
            	params.add("5");
            	EntityRef e = null;
            	try {
					ca.execute(params, e);
					System.out.println("\n C: \n");
				} catch (CommandExecutionException e1) {
					System.out.println("\nNo se pudo realizar el análisis\n");
				}
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

    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }
}
