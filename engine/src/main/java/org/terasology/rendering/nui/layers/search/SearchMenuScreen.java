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

	@Override
	public void initialise() {

		final UIText searchText = find("searchTextBox", UIText.class);		
		ConsoleCommand textSearchCommand = console.getCommand(new Name("searchText"));
		ConsoleCommand classSearchCommand = console.getCommand(new Name("search"));

		WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
			@Override
			public void onActivated(UIWidget button) {
				config.save();
				getManager().popScreen();
				getManager().pushScreen("pauseMenu");
			}
		});
		WidgetUtil.trySubscribe(this, "textSearch", new ActivateEventListener() {
			@Override
			public void onActivated(UIWidget widget) {
				EntityRef e = null;
				ArrayList<String> parameters = new ArrayList<String>();

				parameters.add(searchText.getText());
				try {
					if (!parameters.get(0).equals("")) {
						textSearchCommand.execute(parameters, e);
						searchText.setText("");
						getManager().popScreen();
					}
				} catch (CommandExecutionException e1) {
				}

			}
		});

		WidgetUtil.trySubscribe(this, "classSearch", new ActivateEventListener() {
			@Override
			public void onActivated(UIWidget widget) {
				EntityRef e = null;
				ArrayList<String> parameters = new ArrayList<String>();
				String className = searchText.getText();
				int lastIndex = className.length();
				String classNameEnd = className.substring(lastIndex - 5, lastIndex);
				System.out.println(classNameEnd);
				System.out.println(classNameEnd.equals(".java"));
				if (!classNameEnd.equals(".java")) {
					className += ".java";
				}
				parameters.add(className);
				try {
					if (!parameters.get(0).equals("")) {
						classSearchCommand.execute(parameters, e);
						searchText.setText("");
						getManager().popScreen();
					}
				} catch (CommandExecutionException e1) {
				}

			}
		});
	}
}
