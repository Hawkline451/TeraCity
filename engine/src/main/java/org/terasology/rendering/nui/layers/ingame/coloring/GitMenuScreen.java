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


public class GitMenuScreen extends CoreScreenLayer{

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
	        
	        UIDropdown<ColorScale> colorScale = find("colorScale", UIDropdown.class);
	        if (colorScale != null) {
	        	colorScale.setOptions(Lists.newArrayList(ColorScale.RAINBOW,ColorScale.RED, ColorScale.ORANGE,ColorScale.YELLOW,ColorScale.GREEN,ColorScale.BLUE));
	        }
	        
	        // displays info to the user: warnings, errors, ...
	        final UILabel infoField = find("infoField", UILabel.class);
	        
	        WidgetUtil.trySubscribe(this, "bugs", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	FaceToPaint face = faceToPaint.getSelection();
	            	ColorScale color = colorScale.getSelection();
	            	executeCommand("bugs", face, color);
	            }
	        });
	        WidgetUtil.trySubscribe(this, "versions", new ActivateEventListener() {
	            @Override
	            public void onActivated(UIWidget widget) {
	            	FaceToPaint face = faceToPaint.getSelection();
	            	ColorScale color = colorScale.getSelection();
	            	executeCommand("versions", face, color);
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
        		infoField.setText("waning: please choose a face and a color to paint!");
        		return;
        	}
        	infoField.setText("");
	    	
	    	ConsoleCommand ca = console.getCommand(new Name("paintWithGit"));	
	    	ArrayList<String> params = new ArrayList<String>();
	    	String url="";
	    	String metricString = metric;
			String projectName = "WorldCodecity";
			params.add(metricString);
	    	params.add(url);
	    	params.add(projectName);
	    	params.add(face.toString());
	    	params.add(color.toString());
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
