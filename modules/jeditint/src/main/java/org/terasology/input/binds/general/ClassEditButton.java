package org.terasology.input.binds.general;

import org.terasology.input.BindButtonEvent;
import org.terasology.input.DefaultBinding;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;
import org.terasology.input.RegisterBindButton;

/**
 * Button to open a windows into Minecraft and edit a Class
 * @author Francisco Pulgar
 */
@RegisterBindButton(id = "editclass", description = "Edit Class", category = "general")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.K)
public class ClassEditButton extends BindButtonEvent {
}
