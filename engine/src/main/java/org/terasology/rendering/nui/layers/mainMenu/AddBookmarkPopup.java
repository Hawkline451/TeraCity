package org.terasology.rendering.nui.layers.mainMenu;

import org.terasology.engine.ComponentSystemManager;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UILabel;
import org.terasology.rendering.nui.widgets.UIText;

public class AddBookmarkPopup extends CoreScreenLayer {

	private UILabel message;
	private UIText bookmarkClass;
	private UIText bookmarkName;
	
	
	@Override
	protected void initialise() {
		message = find("message",UILabel.class);
		bookmarkClass = find("class", UIText.class);
		bookmarkName = find("name", UIText.class);
		WidgetUtil.trySubscribe(this, "cancel", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                getManager().popScreen();
                message.setVisible(false);
            }
        });
		WidgetUtil.trySubscribe(this, "add", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
            	ComponentSystemManager csm = CoreRegistry.get(ComponentSystemManager.class);
                ISearchCommands sc = (ISearchCommands) csm.get("FlyMode:SearchCommands");
                if(sc.addBookmarkBool(bookmarkClass.getText(), bookmarkName.getText())){
                	message.setText("Bookmarked!");
                	bookmarkClass.setText("");
                	bookmarkName.setText("");
                	message.setVisible(true);
                } else {
                	message.setText("Class not found.");
                	message.setVisible(true);
                }
            }
        });
		
		
	}

}
