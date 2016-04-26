package org.terasology.rendering.nui.layers.mainMenu;

import org.terasology.engine.ComponentSystemManager;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;


public class BookmarksMenuScreen extends CoreScreenLayer {

	@Override
	protected void initialise() {
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                getManager().popScreen();
            }
        });
        WidgetUtil.trySubscribe(this, "addBookmark", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                getManager().pushScreen("addBookmarkPopup");
            }
        });
	}

}
