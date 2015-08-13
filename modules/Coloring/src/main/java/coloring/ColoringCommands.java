package coloring;

import java.io.IOException;

import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;

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
public class ColoringCommands {
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
            helpText = "Ejecuta colore usando CheckStyle\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCheckStyle(@CommandParam String path,
    		@CommandParam String metric, @CommandParam String max) {
		String[] params = {path, metric, max};
    	IColoring c = new CheckStyleColoring();
    	c.execute(params);
    	return "";
    }
}
