package commandRunner;


import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.ConsoleCommand;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.naming.Name;


public class CommandRunner {
	
	private static CommandRunner instance = new CommandRunner();
	private CommandRunner(){
	}
	public static CommandRunner getInstance(){
		return instance;
	}
	
	public void execute(String commandName, Console console, LocalPlayer localPlayer) throws CommandExecutionException
	{
		if(console == null){
			System.out.println("Consola es null");
			return;
		}
		ConsoleCommand command = console.getCommand(new Name(console.processCommandName(commandName)));
		System.out.println(commandName);
		if(command!=null)
		{
			System.out.println(console.processParameters(commandName));
			command.execute(console.processParameters(commandName), localPlayer.getClientEntity());
		}
		
	}
}
