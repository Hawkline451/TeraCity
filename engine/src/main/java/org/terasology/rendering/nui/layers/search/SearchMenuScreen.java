package org.terasology.rendering.nui.layers.search;

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
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UICheckbox;
import org.terasology.rendering.nui.widgets.UIText;

/**
 * Controller class for the search menu which appears in the pause menu.
 * 
 * @author Pablo
 *
 */
public class SearchMenuScreen extends CoreScreenLayer {

	@In
	private Config config;

	@In
	private Console console;

	// SearchFlag : True if it's searching classes, false in other cases.
	private Boolean searchFlag = new Boolean(true);

	@Override
	public void initialise() {

		final UIText searchText = find("searchTextBox", UIText.class);
		ConsoleCommand cmd = console.getCommand(new Name("search"));

		WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
			@Override
			public void onActivated(UIWidget button) {
				config.save();
				getManager().popScreen();
				getManager().pushScreen("pauseMenu");
			}
		});
		WidgetUtil.trySubscribe(this, "searchButton", new ActivateEventListener() {
			@Override
			public void onActivated(UIWidget widget) {
				EntityRef e = null;
				ArrayList<String> parameters = new ArrayList<String>();

				parameters.add(searchText.getText());
				try {
					if (!parameters.get(0).equals("")) {
						cmd.execute(parameters, e);
						getManager().popScreen();
					}
				} catch (CommandExecutionException e1) {
				}

			}
		});
		
		//Class/Text search buttons.
		
		
    	UICheckbox classSearchBox = find("classSearch", UICheckbox.class);
    	if (classSearchBox != null) {
    		classSearchBox.setChecked(true);
    	}
    	
    	UICheckbox textSearchBox= find("textSearch", UICheckbox.class);
    	if (textSearchBox != null) {
    		textSearchBox.setChecked(false);
    	}
    	/*
		WidgetUtil.tryBindCheckbox(this, "classSearch", new Binding<Boolean>() {

			@Override
			public void set(Boolean value) {
				setSearchFlag(new Boolean (true));
				console.getCommand(new Name("search"));
				textSearch.setChecked(false);
				classSearch.setChecked(true);				
			}

			@Override
			public Boolean get() {
				return getSearchFlag();
			}
		});
		*/
		WidgetUtil.tryBindCheckbox(this, "textSearch", new Binding<Boolean>() {

			@Override
			public void set(Boolean value) {
				setSearchFlag(new Boolean(!value));
				textSearchBox.setChecked(!value);
				classSearchBox.setChecked(value);
				console.getCommand(new Name("textSearch"));
			}

			@Override
			public Boolean get() {
				return getSearchFlag();

			}
		});

	}

	private void setSearchFlag(Boolean val) {
		searchFlag = val;
	}

	private Boolean getSearchFlag() {
		return searchFlag;
	}
}
