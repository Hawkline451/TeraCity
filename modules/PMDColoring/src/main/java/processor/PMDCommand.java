package processor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.In;


@RegisterSystem
public class PMDCommand extends BaseComponentSystem{
    
	@In
    private Console console;
	
	@In
    private LocalPlayer localPlayer;
	
	
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
}

class ThreadPMDExecution implements Runnable
{
	private String sourcePath;
	private String outPutType;
	private String rules;
	private Console console;

	public ThreadPMDExecution(String sourcePath, String outPutType, String rules, Console console) {
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
		
		StringBuilder pmdRoute = new StringBuilder();
		pmdRoute.append('.');
		pmdRoute.append(separator);
		pmdRoute.append("modules");
		pmdRoute.append(separator);
		pmdRoute.append("PMDColoring");
		pmdRoute.append(separator);
		pmdRoute.append("libs");
		pmdRoute.append(separator);
		pmdRoute.append("pmd");
		pmdRoute.append(separator);
		
		StringBuilder sb = new StringBuilder();
		sb.append("java -cp ");
		sb.append(beforePath);
		sb.append(".");
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
		sb.append(" -R ");
		sb.append(pmdRoute);
		sb.append("Metrics");
		sb.append(separator);
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
			 console.addMessage(line);
			 ++messageLines;
			}
			console.addMessage("Lineas del mensage: "+ messageLines);
			
			int totalLines = new LineCounter(LineCounter.JAVA_REGEX).countLines(sourcePath);
			
			console.addMessage("Lineas totales: "+ totalLines);
			/* 
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
			*/
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
