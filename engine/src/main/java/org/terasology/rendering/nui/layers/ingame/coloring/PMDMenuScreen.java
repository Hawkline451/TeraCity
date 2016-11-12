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
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIDropdown;
import org.terasology.rendering.nui.widgets.UILabel;

import com.google.common.collect.Lists;

public class PMDMenuScreen extends CoreScreenLayer {

    @In
    private Config config;
    
    @In
    private Console console;

    @Override
    @SuppressWarnings("unchecked")
    public void initialise() {
        

    	final UIDropdown<FaceToPaint> faceToPaint = find("faceToPaint", UIDropdown.class);
        if (faceToPaint != null) {
            faceToPaint.setOptions(Lists.newArrayList(FaceToPaint.ALL, FaceToPaint.NORTH, FaceToPaint.EAST, FaceToPaint.WEST, FaceToPaint.SOUTH));
        }
        
        final UIDropdown<ColorScale> colorScale = find("colorScale", UIDropdown.class);
        if (colorScale != null) {
        	colorScale.setOptions(Lists.newArrayList(ColorScale.RAINBOW,ColorScale.RED, ColorScale.ORANGE,ColorScale.YELLOW,ColorScale.GREEN,ColorScale.BLUE));
        }
        
        // displays info to the user: warnings, errors, ...
        final UILabel infoField = find("infoField", UILabel.class);
        
        WidgetUtil.trySubscribe(this, "comments", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("comments", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "commentrequired", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("commentrequired", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "commentsize", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("commentsize", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "commentcontent", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("commentcontent", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "codesize", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("codesize", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "npathcomplexity", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("npathcomplexity", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "cyclomaticcomplexity", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("cyclomaticcomplexity", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "toomanymethods", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("toomanymethods", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "coupling", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("coupling", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "couplingbetweenobjects", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("couplingbetweenobjects", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "excessiveimports", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("excessiveimports", face, color);
            }
        });
        WidgetUtil.trySubscribe(this, "lawofdemeter", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	FaceToPaint face = faceToPaint.getSelection();
            	ColorScale color = colorScale.getSelection();
            	executeCommand("lawofdemeter", face, color);
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
    
    private void executeCommand(String metric, FaceToPaint face, ColorScale color) {
    	
    	// manage invalid face selections
    	final UILabel infoField = find("infoField", UILabel.class);
    	if (face == null || color == null) {
    		infoField.setText("Warning: Please choose a face and a color to paint!");
    		return;
    	}
    	infoField.setText("");
    	
    	// send paint command
    	ConsoleCommand ca = console.getCommand(new Name("paintWithPMD"));
    	ArrayList<String> params = new ArrayList<String>();
    	params.add(metric);
    	params.add(face.toString());
    	params.add(color.toString());
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
