package org.terasology.rendering.nui.layers.ingame;

import org.terasology.engine.GameEngine;
import org.terasology.engine.modes.StateMainMenu;
import org.terasology.logic.console.commands.CoreCommands;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;

/**
 * @author Francisco Pulgar Romero
 */

public class EditClassScreen extends CoreScreenLayer{

    @Override
    public void initialise() {
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                getManager().closeScreen(EditClassScreen.this);
            }
        });
        WidgetUtil.trySubscribe(this, "settings", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                getManager().pushScreen("settingsMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "mainMenu", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                CoreRegistry.get(GameEngine.class).changeState(new StateMainMenu());
            }
        });
        WidgetUtil.trySubscribe(this, "fullscreen", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	CoreCommands c = new CoreCommands();
            	c.fullscreen();
            }
        });
        WidgetUtil.trySubscribe(this, "colorear", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                getManager().pushScreen("coloringMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "bookmarks", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	getManager().pushScreen("bookmarksMenuScreen");
            }
        });
        WidgetUtil.trySubscribe(this, "exit", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
                CoreRegistry.get(GameEngine.class).shutdown();
            }
        });
    }	
	
}
