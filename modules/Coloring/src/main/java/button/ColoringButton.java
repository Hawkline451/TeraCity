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

	boolean coloring = false;
	
    ColoringCommands cc = new ColoringCommands();
	
	@Override
    public void initialise() {
        toggleButtonsClientSystem.registerToggleButton(this);
    }
	@Override
	public void toggle() {
		
	}

	@Override
	public boolean isValid() {
		return true;
	}
	
	@Override
	public String getText() {
		if (ColoringCommands.STATE.equals("Coloreando...") && !coloring) {
			cc.applyColoring();
			coloring = true;
		} else if (ColoringCommands.STATE.equals("Esperando Análisis")) {
			coloring = false;
		} 
		return ColoringCommands.STATE;
	}
}
