import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;


@RegisterSystem
public class PMDCommand extends BaseComponentSystem{
    private final CodeScale scale = new SquareRootCodeScale();
    private final CodeMapFactory factory = new CodeMapFactory(scale);
	@In
    private Console console;
	@Command(shortDescription = "PMD coloring.",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String pmdColoring(@CommandParam(value = "sourcePath",required = true) String sourcePath,@CommandParam(value="rules",required=false) String rules,@CommandParam(value="outPutType",required=false) String outPutType) throws IOException
    {
    	if (rules == null) rules = "basic";
    	if (outPutType == null) outPutType = "text";
    	Thread t = new Thread(new ThreadPMDExecution(sourcePath, outPutType, rules,console));
		t.start();
		return "Esperando por resultados del analisis...";
    }
	@Command(shortDescription = "Colors the city based on the result of the metric")
    public String applyColoring() {
		String color = ThreadPMDExecution.getColor();
		if(color==null) return "Color no seteado";
    	BlockFamily blockFamily = getBlockFamily(color);
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	processMap(map, Vector2i.zero(), 10, world, blockFamily);//10 default ground level
            return "Success";
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }
	private void processMap(CodeMap map, Vector2i offset, int level, WorldProvider world, BlockFamily blockFamily) {
        for (MapObject obj : map.getMapObjects()) {
            int x = obj.getPositionX() + offset.getX();
            int y = obj.getPositionZ() + offset.getY();
            int height = obj.getHeight(scale, factory) + level;

            for (int z = level; z < height; z++)
            	world.setBlock(new Vector3i(x, z, y), blockFamily.getArchetypeBlock());
            if (obj.isOrigin())
                processMap(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), height, world, blockFamily);
        }
    }
	private BlockFamily getBlockFamily(String colorBlock) {
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        List<BlockUri> matchingUris = blockManager.resolveAllBlockFamilyUri(colorBlock);
        BlockFamily blockFamily = blockManager.getBlockFamily(matchingUris.get(0));
        return blockFamily;
	}
}

class ThreadPMDExecution implements Runnable
{
	private String sourcePath;
	private String outPutType;
	private String rules;
	private Console console;

	public ThreadPMDExecution(String sourcePath, String outPutType, String rules, Console console) {
		// TODO Auto-generated constructor stub
		this.sourcePath = sourcePath;
		this.outPutType = outPutType;
		this.rules = rules;
		this.console = console;
	}
	private static String currentColor;
	public static String getColor() {
		return currentColor;
	}

	private String buildInputString() {
		
		String OS = System.getProperty("os.name");
		String beforePath = null;
		String separator = null;
		if (OS.startsWith("Linux"))
		{
			beforePath = ":";
			separator = "/";
		}
		else if (OS.startsWith("Windows"))
		{
			beforePath = "";
			separator = "\\";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("java -cp ");
		sb.append(beforePath);
		sb.append('.');
		sb.append(separator);
		sb.append("modules");
		sb.append(separator);
		sb.append("PMDColoring");
		sb.append(separator);
		sb.append("libs");
		sb.append(separator);
		sb.append("pmd");
		sb.append(separator);
		sb.append("lib");
		sb.append(separator);
		sb.append("* net.sourceforge.pmd.PMD -d ");
		sb.append(sourcePath);
		sb.append(" -f ");
		sb.append(outPutType);
		sb.append(" -R rulesets/java/");
		sb.append(rules);
		sb.append(".xml");
		return sb.toString();
	}

	
	@Override
	public void run() 
	{
		
		String inputString = buildInputString();
		try {
			Process process;
			process = Runtime.getRuntime().exec(inputString);
			InputStream is = process.getInputStream();
			 InputStreamReader isr = new InputStreamReader(is);
			 BufferedReader br = new BufferedReader(isr);
				
			 String line;
			 int messageLines = 0;
			 while ((line = br.readLine()) != null) 
			 {
				 //console.addMessage(line);
				 ++messageLines;
			 }
			 console.addMessage("Lineas del mensage: "+ messageLines);
			 
			 int totalLines = new LineCounter(LineCounter.JAVA_REGEX).countLines(sourcePath);
			 
			 console.addMessage("Lineas totales: "+ totalLines);
			 if(rules.equals("comments"))
			 {
				 String color = new CommentsMetric(messageLines, totalLines).getColor();
				 currentColor = color;
				 console.addMessage(color);
			 }
			 else if(rules.equals("codesize"))
			 {
				 String color = new CodeSizesMetric(messageLines, totalLines).getColor();
				 currentColor = color;
				 console.addMessage(color);
			 }
			 console.addMessage("Fin del Analisis");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} 

class LineCounter{
    static Pattern BACKUP_FILE_REGEX = Pattern.compile(".*~");
    public static String JAVA_REGEX = ".*\\.java";
    public static String ALL_REGEX = ".*";
    Pattern pat;
    public LineCounter(String regex){
        pat = Pattern.compile(regex);
    }
    public int countLines(String s){

        try {
            return recCount(new File(s), new HashSet<String>());
        } catch (IOException e) {
            return -1;
        }
    }
    private int recCount(File f, Set<String> files) throws IOException{
        System.out.println(f.getCanonicalPath());
        if(f.getCanonicalPath() == null || files.contains(f.getCanonicalPath()))
            return 0;
        files.add(f.getCanonicalPath());
        System.out.println(f.getCanonicalPath());
        if(!f.isDirectory()) return countLines(f);
        int r=0;
        for(File c:f.listFiles()){
            r+= recCount(c, files);
        }
        return r;
    }
    private int countLines(File f) throws IOException {
        if(f.getCanonicalPath() == null)
            return 0;
        if(!pat.matcher(f.getCanonicalPath()).matches()) return 0;
        int i=0;
        try {
            BufferedReader br= new BufferedReader(new FileReader(f));

            for(String line=br.readLine() ;line !=null;line = br.readLine()){
                i++;
            }
            br.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return i;
    }
}
