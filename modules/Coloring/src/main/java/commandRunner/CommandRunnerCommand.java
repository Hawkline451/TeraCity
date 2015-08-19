package commandRunner;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;

@RegisterSystem
public class CommandRunnerCommand extends BaseComponentSystem{

	@In
	private Console console;
	
	@In
	private LocalPlayer localPlayer;

	@Command(shortDescription = "Runs another command")
    public String runCommand(@CommandParam(value = "sourcePath",required = true) String commandName) throws CommandExecutionException {
		CommandRunner.getInstance().execute(commandName, console, localPlayer);
		
		return commandName;
	}
}
