package org.terasology.input.binds.general;

import org.terasology.input.BindButtonEvent;
import org.terasology.input.DefaultBinding;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;
import org.terasology.input.RegisterBindButton;

/**
 * Button to open Jedit
 * @author Patricio Huepe
 */
@RegisterBindButton(id = "jedit", description = "Open jEdit", category = "general")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.J)
public class JeditButton extends BindButtonEvent {
}
