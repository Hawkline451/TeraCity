package org.terasology.rendering.nui.layers.mainMenu;

import java.util.ArrayList;
import java.util.Arrays;

import org.terasology.config.Config;
import org.terasology.engine.ComponentSystemManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.ConsoleCommand;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.naming.Name;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.databinding.BindHelper;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIDropdown;


public class VariableWidthMenuScreen extends CoreScreenLayer{
	
	private ISearchCommands sc;
	
	private CoreScreenLayer parent;
	
	@In
    private Config config;
    
    @In
    private Console console;
	

	@Override
    @SuppressWarnings("unchecked")
	protected void initialise() {
        
        UIDropdown<VerticalScaleType> verticalScaleType = find("verticalScaleType", UIDropdown.class);
        if (verticalScaleType != null) {
        	verticalScaleType.setOptions(Arrays.asList(VerticalScaleType.LINEAR, VerticalScaleType.HALFLINEAR, VerticalScaleType.SQRT));
        	verticalScaleType.bindSelection(BindHelper.bindBeanProperty("verticalScaleType", config.getRendering(), VerticalScaleType.class));
        }

        UIDropdown<HorizontalScaleType> horizontalScaleType = find("horizontalScaleType", UIDropdown.class);
        if (horizontalScaleType != null) {
        	horizontalScaleType.setOptions(Arrays.asList(HorizontalScaleType.LINEAR, HorizontalScaleType.HALFLINEAR, HorizontalScaleType.SQRT));
        	horizontalScaleType.bindSelection(BindHelper.bindBeanProperty("horizontalScaleType", config.getRendering(), HorizontalScaleType.class));
        }
        
		ComponentSystemManager csm = CoreRegistry.get(ComponentSystemManager.class);	
		
		WidgetUtil.trySubscribe(this, "applyWidths", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
            	VerticalScaleType vst = verticalScaleType.getSelection();
            	HorizontalScaleType hst = horizontalScaleType.getSelection();
            	executeCommand(vst, hst);
            }
        });
		
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                getManager().popScreen();
            }
        });
	}
	
	
	private void executeCommand(VerticalScaleType vst, HorizontalScaleType hst) {
    	
    	ConsoleCommand command1 = console.getCommand(new Name("changeHeightScale"));
    	ArrayList<String> params1 = new ArrayList<String>();
    	
    	ConsoleCommand command2 = console.getCommand(new Name("changeWidthScale"));
    	ArrayList<String> params2 = new ArrayList<String>();
    	
    	EntityRef e = null;
    	try {
        	params1.add(vst.toString());
    		command1.execute(params1, e);
    	} catch (CommandExecutionException e1) {
    	} catch (NullPointerException e2) {
    	}
    	try {
        	params2.add(hst.toString());
    		command2.execute(params2, e);
    	} catch (CommandExecutionException e1) {
    	} catch (NullPointerException e2) {
    	}
    }
	
	@Override
	public void onOpened(){
		super.onOpened();
	}
	
	public void setParent(CoreScreenLayer p) {
		parent = p;
	}
}
