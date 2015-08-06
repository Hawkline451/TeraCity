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
	
	@Command(shortDescription = "Muestra en consola la simplificacion del parseo del ultimo analisis")
	public String showParse() throws IOException {
		String path = "modules/CheckStyle/Project/out.xml";
		CheckStyleParser cp = new CheckStyleParser();
		cp.parse(path);
		return cp.toString();
	}
	
	@Command(shortDescription = "Utiliza checkstyle para analizar un archivo", 
			 helpText = "Utiliza checkStyle para para analizar algun programa segun la metrica dada \n"
			 		+ "cstyle archivo metrica maxValue \n"
			 		+ "Donde:\n"
			 		+ "    archivo: es el archivo a analizar \n"
			 		+ "    metrica: b para booleana, ciclomatica por defecto \n"
			 		+ "    maxValue: maximo valor de comparadores en caso de metrica booleana")
	public String cstyle(@CommandParam("Archivo") String path, 
						 @CommandParam("Metrica") String metricString,
						 @CommandParam("Maximo valor booleano") Integer max) throws IOException {
		
		Metric metric = Metric.createMetric(metricString, max);
		metric.execute(path, console);
		return "";
	}

}