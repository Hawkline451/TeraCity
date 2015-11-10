package coloringCommands;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.CoreRegistry;

import com.google.common.util.concurrent.ListenableFuture;

import coloring.AbstractColoring;
import coloring.CheckStyleColoring;
import coloring.CoberturaColoring;
import coloring.GitColoring;
import coloring.IColoring;
import coloring.MockColoring;
import coloring.PMDColoring;

@RegisterSystem
/**
 * Los metodos de Coloring deberian ir aca y seguir un formato similar al del
 * comando ya implementado: Recibir parametros, guardarlos en una lista, 
 * instanciar el objeto adecuado de la jerarquia de AbstractColoring y usar su
 * metodo execute, pasandole la lista de parametros guardada.
 *
 * Cada uno de estos metodos sera luego llamado por su boton correspondiente
 * en el submenu de coloreo.
 * (ej: /engine/src/main/java/org/terasology/rendering/nui/layers/mainMenu/CoberturaMenuScreen.java)
 */
public class ColoringCommands extends BaseComponentSystem{
	
	public static String STATE = "Awaiting analisys";
	
	@Command(shortDescription = "Coloreo usando Cobertura",
            helpText = "Ejecuta coloreo de Cobertura, con el input de la forma especificada, "
            		+ "sobre los archivos especificados\n"
                    + "type: Formato de input a utilizar:\n"
                    + " - \"-t\": Archivos testeados y de tests en dos carpetas separadas\n"
                    + " - \"-s\": Todos los archivos en una sola carpeta (ayuda a no hacer procesos "
                    + "en archivos donde no sean necesarios).\n"
                    + " - \"-r\": Se entrega un path directamente a un reporte de Cobertura a analizar ("
                    + "al reporte en si mismo no solo a la carpeta que lo contiene).\n"
                    + "firstArg: Primer argumento. Si se elige el tipo 1, son los archivos a testear.\n"
                    + "secondArg: Segundo argumento. Si se elige el tipo 2, son los archivos de tests,"
                    + "no se usa para los otros dos tipos.",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCobertura(
    		@CommandParam(value="type",required=true) String type,
            @CommandParam(value="firstArg",required=true) String firstArg,
            @CommandParam(value="secondArg",required=false) String secondArg) throws IOException{
		CoberturaColoring cob = new CoberturaColoring();
		String[] pars = new String[3];
		pars[0] = type;
		pars[1] = firstArg;
		pars[2] = secondArg;
		cob.execute(pars);
		return "Loading ...";
    }
	
	@Command(shortDescription = "Coloreo usando CheckStyle",
            helpText = "Ejecuta coloreo usando CheckStyle\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCheckStyle(@CommandParam("Ruta") String path,
    		@CommandParam("Metrica") String metric, @CommandParam("Valor Maximo") String max) {
		if (path.equals("default")) path = "./";
		String[] params = {path, metric, max};
    	IColoring c = new CheckStyleColoring();
    	c.execute(params);
    	return "";
    }
	@Command(shortDescription = "Coloreo usando Git",
            helpText = "Ejecuta coloreo usando Git\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithGit(@CommandParam("metric") String metric,
    		@CommandParam("url") String url, @CommandParam("Project Name") String projectName) {
		
		String[] params = {metric, url, projectName};
    	IColoring c = new GitColoring();
    	c.execute(params);
    	return "";
    }
	
	@Command(shortDescription = "Coloreo usando PMD",
            helpText = "Ejecuta coloreo usando PMD y dando como argumento la regla correspondiente\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithPMD(@CommandParam(value = "rule",required = true) String rule) {
		String[] params = {rule};
    	IColoring c = new PMDColoring();
    	c.execute(params);
    	return "";
    }
	
	@Command(shortDescription = "Mock coloring",
			helpText = "Mock coloring based on MockColoring\n",
			requiredPermission = PermissionManager.NO_PERMISSION)
	public String paintWithMockColoring(
			@CommandParam(value="metric", required=true) String metric) {
		
		String[] params = new String[1];
		params[0] = metric;
		IColoring coloring = new MockColoring();
		coloring.execute(params);
		return "";
	}
	
	@Command(shortDescription = "Aplica el coloreo",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String applyColoring() {
		ListenableFuture<AbstractColoring> listeneableFuture = CoreRegistry.get(ListenableFuture.class);
		if (listeneableFuture == null || !listeneableFuture.isDone()) return "Analisis no terminado";
		try {
			listeneableFuture.get().executeColoring();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Error de Pintado");
		}
		return "Pintado";
    }
}
