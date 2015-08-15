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
import coloring.IColoring;
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
	@Command(shortDescription = "Coloreo usando Cobertura",
            helpText = "Ejecuta colore usando Cobertura sobre los archivos especificados\n"
                    + "<filesFolder>: Archivos que son testeados\n"
                    + "<testsFolder>: Archivos de test\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCobertura(
            @CommandParam(value = "filesFolder",required = true) String filesFolder,
            @CommandParam(value="testsFolder",required=true) String testsFolder) throws IOException{
		CoberturaColoring cob = new CoberturaColoring();
		String[] pars = new String[2];
		pars[0] = filesFolder;
		pars[1] = testsFolder;
		cob.execute(pars);
		return "Loading ...";
    }
	
	@Command(shortDescription = "Coloreo usando CheckStyle",
            helpText = "Ejecuta coloreo usando CheckStyle\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCheckStyle(@CommandParam("Ruta") String path,
    		@CommandParam("Métrica") String metric, @CommandParam("Valor Máximo") String max) {
		String[] params = {path, metric, max};
    	IColoring c = new CheckStyleColoring();
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
