package org.terasology.rendering.nui.layers.search;

import org.terasology.config.Config;
import org.terasology.logic.console.Console;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;

public class SearchMenu extends CoreScreenLayer{

    @In
    private Config config;
    
    @In
    private Console console;
    
    public void initialise() {
    	
        WidgetUtil.trySubscribe(this, "search", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
            }
        });
    	
    }

}
