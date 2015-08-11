package speedAlgorithm;

import org.terasology.logic.console.Console;
import org.terasology.registry.In;

public abstract class Speed {
    @In
    Console console;
	
	abstract float calculateSpeed();
	
	public abstract float getCalculatedSpeed();
	
}
