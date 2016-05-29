package org.terasology.rendering.nui.layers.mainMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.terasology.engine.ComponentSystemManager;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.layouts.ColumnLayout;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.UILabel;


public class VariableWidthMenuScreen extends CoreScreenLayer{
	
	private ISearchCommands sc;
	
	private CoreScreenLayer parent;
	

	@Override
	protected void initialise() {
		ComponentSystemManager csm = CoreRegistry.get(ComponentSystemManager.class);		
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                getManager().popScreen();
            }
        });
	}
	
	@Override
	public void onOpened(){
		super.onOpened();
	}
	
	public void setParent(CoreScreenLayer p) {
		parent = p;
	}
}
