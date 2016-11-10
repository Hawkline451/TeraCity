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
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UICheckbox;

public class ColoringMenuScreen extends CoreScreenLayer {

    @In
    private Config config;
    
    @In
    private Console console;

    @Override
    public void initialise() {
    	
    	UICheckbox renderQuakes = find("renderQuakes", UICheckbox.class);
    	if (renderQuakes != null) {
    		renderQuakes.setChecked(true);
    	}
    	
    	WidgetUtil.tryBindCheckbox(this, "renderQuakes", new Binding<Boolean>() {

    		private Boolean isChecked = new Boolean(true);
    		
			@Override
			public void set(Boolean value) {
				isChecked = value;
                sendUpdateRegistryCommand(value);
			}
			
			@Override
			public Boolean get() {
				return isChecked;
			}
		});
    	
        WidgetUtil.trySubscribe(this, "checkStyle", new ActivateEventListener() {
        	  @Override
              public void onActivated(UIWidget button) {
                  getManager().pushScreen("engine:checkStyleMenuScreen");
              }
        });
        WidgetUtil.trySubscribe(this, "pmd", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	getManager().pushScreen("engine:pMDMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "cobertura", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	getManager().pushScreen("engine:coberturaMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "git", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	getManager().pushScreen("engine:gitMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "mockColoring", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	getManager().pushScreen("engine:mockColoringMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                config.save();
                getManager().popScreen();
            }
        });
        // Nuevo intento .
        WidgetUtil.trySubscribe(this, "search", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
            	;
            }
        });
    }
    
    private void sendUpdateRegistryCommand(Boolean renderQuakes) {

    	ConsoleCommand command = console.getCommand(new Name("updateColoringState"));
    	ArrayList<String> params = new ArrayList<String>();
    	params.add(renderQuakes.toString());

    	EntityRef e = null;
    	try {
    		command.execute(params, e);
    	} catch (CommandExecutionException e1) {
    	}
    }

    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }
}
