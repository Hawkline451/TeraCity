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
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UILabel;


public class BookmarksMenuScreen extends CoreScreenLayer{
	
	private ISearchCommands sc;
	
	private ColumnLayout bookmarkList;
	
	private ArrayList<String> bookmarkNames = new ArrayList<>();

	@Override
	protected void initialise() {
		ComponentSystemManager csm = CoreRegistry.get(ComponentSystemManager.class);
        sc = (ISearchCommands) csm.get("FlyMode:SearchCommands");
		HashMap<String,String> bookmarks = sc.getBookmarks();
		bookmarkNames.addAll(bookmarks.keySet());
		
		bookmarkList = find("bookmarksList",ColumnLayout.class);
		for(String bookmarkName : bookmarks.keySet()){
			bookmarkList.addWidget(new UILabel(bookmarkName));
			bookmarkList.addWidget(new UILabel (bookmarks.get(bookmarkName)));
		}
		
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
			bookmarkList.addWidget(new UILabel(bookmarkName));
			bookmarkList.addWidget(new UILabel (bookmarks.get(bookmarkName)));
			bookmarkNames.add(bookmarkName);
		}
	}
}
