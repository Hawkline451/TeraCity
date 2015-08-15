package button;

import org.terasology.HUDToggleButtons.systems.HUDToggleButtonsClientSystem;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;

import coloringCommands.ColoringCommands;

@RegisterSystem
public class ColoringButton extends BaseComponentSystem implements HUDToggleButtonsClientSystem.HUDToggleButtonState{
	
    @In
    private HUDToggleButtonsClientSystem toggleButtonsClientSystem;
    
    public String text = "Colorear";
	
	@Override
    public void initialise() {
        toggleButtonsClientSystem.registerToggleButton(this);
    }
	@Override
	public void toggle() {
		ColoringCommands cc = new ColoringCommands();
		if (cc.applyColoring().equals("Analisis no terminado")) {
			text = "Analizando aun...";
		} else {
			text = "Colorear";
		}
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public String getText() {
		return text;
	}
}
