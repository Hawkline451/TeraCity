package org.terasology.entitySystem.systems;

import java.util.HashMap;

import org.terasology.rendering.nui.layers.mainMenu.BookmarksMenuScreen;

public interface ISearchCommands {

    public boolean addBookmarkBool(String className, String bookName);
    public HashMap<String, String> getBookmarks();
    public String search(String className);
}
