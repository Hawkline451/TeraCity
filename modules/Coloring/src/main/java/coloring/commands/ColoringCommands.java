package coloring.commands;

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
import coloring.ColoringRegistry;
import coloring.IColoring;
import coloring.modules.*;

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
	
	public static String STATE = "Awaiting analysis";

    @Command(shortDescription = "Coloring using Cobertura",
            helpText = "Run color of Cobertura, with input in the specified format, "
                    + "on specified files\n"
                    + "type: input's format to use:\n"
                    + " - \"-t\":  test and tested files in two separate folders\n"
                    + " - \"-s\": All the files in only one folder (Help to do not process "
                    + "in files where it is not necessary)..\n"
                    + " - \"-r\": A path is delivered directly to a Cobertura report to be analyzed ("
                    + "to the report itself not only to the folder containing it)\n"
                    + "firstArg: first argument. If 1 is choosen, it concerns the files to be tested.\n"
                    + "secondArg: Second argument . If 2 is choosen, it concerns the test files,"
                    + "not used for the other two types.",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCobertura(
    		@CommandParam(value="face"     , required=true) String face,
			@CommandParam(value="color"    , required=true) String color,
    		@CommandParam(value="type"     , required=true) String type,
            @CommandParam(value="firstArg" , required=true) String firstArg,
            @CommandParam(value="secondArg", required=false) String secondArg) throws IOException{
		
		String[] pars = new String[3];
		pars[0] = type;
		pars[1] = firstArg;
		pars[2] = secondArg;
		
		IColoring coloring = new CoberturaColoring();
		coloring.setFaceToPaint(face);
		coloring.setColorScale(color);
		coloring.execute(pars);
		return "Loading ...";
    }
	
	@Command(shortDescription = "Coloring using CheckStyle",
            helpText = "Run coloring using CheckStyle\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithCheckStyle(
    		@CommandParam("Path")         String path,
    		@CommandParam("Metric")      String metric,
    		@CommandParam("Maximum value") String max,
    		@CommandParam(value="face"  , required=true) String face,
			@CommandParam(value="color" , required=true) String color) { 

		if (path.equals("default")) {
			path = "./";
		}
		String[] params = {path, metric, max};
    	IColoring coloring = new CheckStyleColoring();
    	coloring.setFaceToPaint(face);
    	coloring.setColorScale(color);
    	coloring.execute(params);
    	return "";
    }
	@Command(shortDescription = "Coloring using Git",
            helpText = "Run coloring using Git\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithGit(
    		@CommandParam("metric") String metric,
    		@CommandParam("url"   ) String url,
    		@CommandParam("Project Name") String projectName,
    		@CommandParam(value="face"  , required=true) String face,
			@CommandParam(value="color" , required=true) String color) {
		
		String[] params = {metric, url, projectName};
    	IColoring coloring = new GitColoring();
    	coloring.setFaceToPaint(face);
    	coloring.setColorScale(color);
    	coloring.execute(params);
    	return "";
    }
	
	@Command(shortDescription = "Coloring using PMD",
            helpText = "Run coloring using PMD giving in the argument the corresponding rule \n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String paintWithPMD(
    		@CommandParam(value="rule" , required=true) String rule,
    		@CommandParam(value="face" , required=true) String face,
			@CommandParam(value="color", required=true) String color) {
		
		String[] params = {rule};
    	IColoring coloring = new PMDColoring();
    	coloring.setFaceToPaint(face);
    	coloring.setColorScale(color);
    	coloring.execute(params);
    	return "";
    }
	
	@Command(shortDescription = "Mock coloring",
			helpText = "Mock coloring based on MockColoring\n",
			requiredPermission = PermissionManager.NO_PERMISSION)
	public String paintWithMockColoring(
			@CommandParam(value="metric", required=true) String metric,
			@CommandParam(value="face"  , required=true) String face,
			@CommandParam(value="color" , required=true) String color) {
		
		String[] params = new String[1];
		params[0] = metric;
		IColoring coloring = new MockColoring();
		coloring.setFaceToPaint(face);
		coloring.setColorScale(color);
		coloring.execute(params);
		return "";
	}
	
	@SuppressWarnings("unchecked")
	@Command(shortDescription = "Apply the coloration",
			requiredPermission = PermissionManager.NO_PERMISSION)
	public String applyColoring() {
		ListenableFuture<AbstractColoring> listeneableFuture = CoreRegistry.get(ListenableFuture.class);
		if (listeneableFuture == null || !listeneableFuture.isDone()) return "Analisis not finished";
		try {
			listeneableFuture.get().executeColoring();
		} catch (InterruptedException | ExecutionException e) {
			System.out.println("Paint error");
		}
		return "Painted";
    }
	
	@Command(
			shortDescription  = "Updates registry with the color status",
            helpText           = "TODO\n",
            requiredPermission = PermissionManager.NO_PERMISSION
	)
    public String updateColoringState(
    		@CommandParam(value="renderQuakes" , required=true) String renderQuakes)
	{
		// update register
		boolean doRenderQuakes = new Boolean(renderQuakes).booleanValue();
		ColoringRegistry registry = ColoringRegistry.getRegister();
		registry.updateState(doRenderQuakes);
		CoreRegistry.put(ColoringRegistry.class, registry);
		
		// refresh
		new PlaceBlockCommand().refreshCity();
		
		return "";
    }
}
