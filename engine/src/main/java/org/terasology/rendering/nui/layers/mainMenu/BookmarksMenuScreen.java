package org.terasology.rendering.nui.layers.mainMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.terasology.engine.ComponentSystemManager;
import org.terasology.entitySystem.systems.ISearchCommands;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.layouts.ColumnLayout;
import org.terasology.rendering.nui.layouts.RowLayout;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIButton;
import org.terasology.rendering.nui.widgets.UILabel;


public class BookmarksMenuScreen extends CoreScreenLayer{
	
	private ISearchCommands sc;
	
	private ColumnLayout bookmarkList;
	private CoreScreenLayer parent;
	
	private ArrayList<String> bookmarkNames = new ArrayList<>();

	@Override
	protected void initialise() {
		ComponentSystemManager csm = CoreRegistry.get(ComponentSystemManager.class);
        sc = (ISearchCommands) csm.get("FlyMode:SearchCommands");
		bookmarkList = find("bookmarksList",ColumnLayout.class);
		bookmarkList.setFillVerticalSpace(false);
		bookmarkList.setColumnWidths(0.4f,0.4f,0.2f);
		
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                getManager().popScreen();
            }
        });
        WidgetUtil.trySubscribe(this, "addBookmark", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                AddBookmarkPopup bp = (AddBookmarkPopup) getManager().pushScreen("addBookmarkPopup");
                bp.suscriber(BookmarksMenuScreen.this);
                String target = sc.getTarget();
                if(target != null)
                	bp.setClassTarget(target);
            }
        });
	}
	
	@Override
	public void onOpened(){
		super.onOpened();
		updateBookmarks();
	}

	public void updateBookmarks(){
		HashMap<String,String> bookmarks = sc.getBookmarks();
		Set<String> newBookmarkNames = bookmarks.keySet();
		newBookmarkNames.removeAll(bookmarkNames);
		for(String bookmarkName : newBookmarkNames){
			String className = bookmarks.get(bookmarkName);
			UIButton button = new UIButton(className,"Go");
			bookmarkList.addWidget(new UILabel(bookmarkName));
			bookmarkList.addWidget(new UILabel (className));
			bookmarkList.addWidget(button);
			button.subscribe(new ActivateEventListener() {
				
				@Override
				public void onActivated(UIWidget widget) {
					getManager().closeScreen(BookmarksMenuScreen.this);
					if(parent != null)
						getManager().closeScreen(parent);
					sc.search(className);
				}
			});
			bookmarkNames.add(bookmarkName);
		}
	}
	
	public void setParent(CoreScreenLayer p) {
		parent = p;
	}
}
