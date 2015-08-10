package speedAlgorithm;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.registry.In;

@RegisterSystem
public abstract class Speed extends BaseComponentSystem{
    @In
    Console console;
	
	abstract float calculateSpeed();
	
	public abstract float getCalculatedSpeed();
	
}
