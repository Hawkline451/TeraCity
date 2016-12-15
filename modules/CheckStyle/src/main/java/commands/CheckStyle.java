package commands;


import java.io.IOException;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.registry.In;

import metrics.*;
import utility.CheckStyleParser;

@RegisterSystem
public class CheckStyle extends BaseComponentSystem{
	
	@In
	Console console;
	
	@Command(shortDescription = "Show in console the simplification of the last analysis's parse")
	public String showParse() throws IOException {
		String path = "modules/CheckStyle/Project/out.xml";
		CheckStyleParser cp = new CheckStyleParser();
		cp.parse(path);
		return cp.toString();
	}
	
	@Command(shortDescription = "Using checkstyle to analyze a file", 
			 helpText = "Using checkstyle to analyse some problems following the given metric \n"
			 		+ "cstyle file metric maxValue \n"
			 		+ "Where:\n"
			 		+ "    file: the file to analyze \n"
			 		+ "    metric: b for boolean, Cyclomatic by default \n"
			 		+ "    maxValue: maximum value of comparators if metric is a boolean")
	public String cstyle(@CommandParam("Path") String path, 
						 @CommandParam("Metric") String metricString,
						 @CommandParam("Maximum boolean value") Integer max) throws IOException {
		
		Metric metric = Metric.createMetric(metricString, max, console);
		metric.execute(path);
		return "";
	}
}