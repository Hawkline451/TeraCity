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
import org.terasology.rendering.nui.widgets.UIText;

import com.google.common.collect.Lists;

/**
 * PMD Coloring UI Manager.
 *
 */
public class PMDMenuScreen extends CoreScreenLayer {

	private final ArrayList<String> validMetrics = Lists.newArrayList("android", "basic", "braces", "clone",
			"codesize", "comments", "controversial", "coupling", "design", "empty", "finalizers", "imports",
			"j2ee", "javabeans", "junit", "logging-jakarta-commons", "logging-java", "migrating",
			"migrating_to_13", "migrating_to_14", "migrating_to_15", "migrating_to_junit4", "naming",
			"optimizations", "strictexception", "strings", "sunsecure", "typeresolution", "unnecessary",
			"unusedcode");
    @In
    private Config config;
    
    @In
    private Console console;

    @Override
    @SuppressWarnings("unchecked")
    public void initialise() {

    	final UILabel infoField = find("infoField", UILabel.class);
    	final UIText filePath = find("filePath", UIText.class);
    	final UIText metricText = find("metric", UIText.class);
    	
    	WidgetUtil.trySubscribe(this, "analyze", new ActivateEventListener() {
			@Override
			public void onActivated(UIWidget widget) {
				String path = filePath.getText();
				String metric = metricText.getText();
				executeAnalysisCommand(path, metric, infoField);
			}
    	});
    	
    	WidgetUtil.trySubscribe(this, "paint", new ActivateEventListener() {
    		@Override
    		public void onActivated(UIWidget widget) {
    			String metric = metricText.getText();
    			executeColoringCommand(metric, infoField);
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
    
    private void executeAnalysisCommand(String path, String metric, UILabel infoField) {
    	if (path.equals("")) {
    		infoField.setText("Warning: You have to write a file path!");
    		return;
    	} else if (metric.equals("") || !validMetrics.contains(metric)) {
    		infoField.setText("Warning: The given metric is not valid!");
    		return;
    	}
    	infoField.setText("");
    	
    	ConsoleCommand ca = console.getCommand(new Name("pmdAnalysis"));
    	ArrayList<String> params = new ArrayList<String>();
    	params.add(path);
    	params.add(metric);
    	try {
    		ca.execute(params, EntityRef.NULL);
    	} catch (CommandExecutionException e) {
    		// Potato
    		infoField.setText("Something went wrong!");
    		return;
    	}
    }
    
    private void executeColoringCommand(String metric, UILabel infoField) {
    	if (metric.equals("") || !validMetrics.contains(metric)) {
    		infoField.setText("Warning: The given metric is not valid!");
    		return;
    	}
    	infoField.setText("");
    	
    	ConsoleCommand ca = console.getCommand(new Name("pmdColoring"));
    	ArrayList<String> params = new ArrayList<String>();
    	params.add(metric);
    	try {
    		ca.execute(params, EntityRef.NULL);
    	} catch (CommandExecutionException e) {
    		// More potatoes
    		infoField.setText("Something went wrong!");
    		return;
    	}
    }

    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }
}
