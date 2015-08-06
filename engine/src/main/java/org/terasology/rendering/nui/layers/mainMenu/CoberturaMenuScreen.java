/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.nui.layers.mainMenu;

import java.util.List;
import java.util.ArrayList;

import org.terasology.asset.AssetType;
import org.terasology.asset.AssetUri;
import org.terasology.asset.Assets;
import org.terasology.config.Config;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.ConsoleCommand;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.logic.console.commands.CoreCommands;
import org.terasology.naming.Name;
import org.terasology.registry.In;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.UIWidget;
import org.terasology.rendering.nui.WidgetUtil;
import org.terasology.rendering.nui.asset.UIData;
import org.terasology.rendering.nui.asset.UIElement;
import org.terasology.rendering.nui.databinding.BindHelper;
import org.terasology.rendering.nui.layers.mainMenu.inputSettings.InputSettingsScreen;
import org.terasology.rendering.nui.widgets.ActivateEventListener;
import org.terasology.rendering.nui.widgets.UIText;

/**
 * @author Immortius
 */
public class CoberturaMenuScreen extends CoreScreenLayer {

    private static final AssetUri INPUT_SCREEN_URI = new AssetUri(AssetType.UI_ELEMENT, "engine:inputScreen");

    @In
    private Config config;
    @In
    private Console console;
    
    @Override
    public void initialise() {
        CoreScreenLayer inputScreen = new InputSettingsScreen();
        inputScreen.setSkin(getSkin());
        UIData inputScreenData = new UIData(inputScreen);
        Assets.generateAsset(INPUT_SCREEN_URI, inputScreenData, UIElement.class);
        
        UIText testClass = find("testClasses", UIText.class);
        UIText testedClass = find("testedClasses", UIText.class);
        
        /* Esto hace un binding entre el texto del textArea y el valor del parámetro "name" en Config
         * y lo guarda el metodo q maneja el boton Close (config.save()).
         * Podrían crearse nuevos parámetros en Config para la ruta de las carpetas de tests y clases 
         * a la hora de crear el mundo (en lugar de pedirlas al usuario) y asi automatizar el proceso
        if (testedText != null) {
            testedText.bindText(BindHelper.bindBeanProperty("name", config.getPlayer(), String.class));
        }*/
        
        WidgetUtil.trySubscribe(this, "analizar", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget widget) {
            	String testee = testedClass.getText();
            	String tests = testClass.getText();
            	ConsoleCommand ca = console.getCommand(new Name("CoberturaAnalysis"));
            	List<String> params = new ArrayList<String>();
            	params.add(testee); params.add(tests);
            	EntityRef e = null;
            	try {
					ca.execute(params, e);
					System.out.println("\n C: \n");
				} catch (CommandExecutionException e1) {
					System.out.println("\n:C You prolly don't have the Cobertura module, yo\n");
				}
            }
        });
        WidgetUtil.trySubscribe(this, "close", new ActivateEventListener() {
            @Override
            public void onActivated(UIWidget button) {
                config.save(); // No hace nada, no hay ningun cambio a la configuración aquí
                getManager().popScreen();
            }
        });
    }

    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }
}
