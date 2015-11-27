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

import java.util.List;
import java.util.ArrayList;

import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
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
import org.terasology.rendering.nui.widgets.UIText;

import com.google.common.collect.Lists;

/**
 * @author Immortius
 */
public class CoberturaMenuScreen extends CoreScreenLayer {

    private static final AssetUri INPUT_SCREEN_URI = new AssetUri(AssetType.UI_ELEMENT, "engine:inputScreen");

    @In
    private Console console;
    
    @Override
    @SuppressWarnings("unchecked")
    public void initialise() {
        CoreScreenLayer inputScreen = new InputSettingsScreen();
        inputScreen.setSkin(getSkin());
        UIData inputScreenData = new UIData(inputScreen);
        Assets.generateAsset(INPUT_SCREEN_URI, inputScreenData, UIElement.class);
        final UIText testClass = find("testClasses", UIText.class);
        final UIText testedClass = find("testedClasses", UIText.class);
        final UIText sourceFolder = find("singleFolder", UIText.class);
        final UIText xmlReport = find("xmlReport", UIText.class);
        
        final UIDropdown<FaceToPaint> faceToPaint = find("faceToPaint", UIDropdown.class);
        if (faceToPaint != null) {
            faceToPaint.setOptions(Lists.newArrayList(FaceToPaint.ALL, FaceToPaint.NORTH, FaceToPaint.EAST, FaceToPaint.WEST, FaceToPaint.SOUTH));
        }
        
        // displays info to the user: warnings, errors, ...
        final UILabel infoField = find("infoField", UILabel.class);
        
        WidgetUtil.trySubscribe(this, "analizar1", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	
            	// Analisis Tipo 1 (Dos Carpetas)
            	String testee = testedClass.getText();
            	String tests = testClass.getText();
            	
            	List<String> params = new ArrayList<String>();
            	params.add("-s");
            	params.add(testee);
            	params.add(tests);
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(params, face);
           }
        });
        WidgetUtil.trySubscribe(this, "analizar2", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	// Analisis Tipo 2 (Una Carpeta)
            	String source = sourceFolder.getText();
            	
            	List<String> params = new ArrayList<String>();
            	params.add("-t");
            	params.add(source);
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(params, face);
           }
        });
        
        WidgetUtil.trySubscribe(this, "analizar3", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	// Analisis Tipo 3 (Reporte)
            	String report = xmlReport.getText();
            	
            	List<String> params = new ArrayList<String>();
            	params.add("-r");
            	params.add(report);
            	
            	FaceToPaint face = faceToPaint.getSelection();
            	executeCommand(params, face);
           }
        });
        
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
            	infoField.setText("");
            	getManager().popScreen();
            }
        });
        
    }
    private void executeCommand(List<String> params, FaceToPaint face){
    	
    	// manage invalid face selections
    	final UILabel infoField = find("infoField", UILabel.class);
    	if (face == null) {
    		infoField.setText("waning: please choose a face to paint!");
    		return;
    	}
    	infoField.setText("");
    	
    	ArrayList<String> cparams = new ArrayList<String>();
    	cparams.add(face.toString());
    	cparams.addAll(params);
    	
    	// send paint command
    	ConsoleCommand ca = console.getCommand(new Name("paintWithCobertura"));
    	EntityRef e = null;
    	try {
    		ca.execute(cparams, e);
    	} catch (CommandExecutionException e1) {
    	}
    }
    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }
}
