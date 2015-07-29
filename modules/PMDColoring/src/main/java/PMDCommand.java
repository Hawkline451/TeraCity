import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;


@RegisterSystem
public class PMDCommand extends BaseComponentSystem{
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
		// TODO Auto-generated method stub
		
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
			 console.addMessage("Message's Lines: "+ messageLines);
			 console.addMessage("Total Lines: "+new LineCounter(LineCounter.JAVA_REGEX).countLines(sourcePath));
			 console.addMessage("Fin del Analisis");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

} 

class LineCounter{
    //static Pattern backup = Pattern.compile(".*~");
    public static String JAVA_REGEX = ".*\\.java";
    Pattern pat;
    public LineCounter(String regex){
        pat = Pattern.compile(regex);
    }
    public int countLines(String s){
        return recCount(new File(s));
    }
   private int recCount(File f){
       if(!f.isDirectory()) return countLines(f);
       int r=0;
       for(File c:f.listFiles()){
           r+= recCount(c);
       }
       return r;
   }
   private int countLines(File f) {
       if(!pat.matcher(f.getPath()).matches()) return 0;
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
   public static void main(String[] args) {
       LineCounter javaFiles = new LineCounter(LineCounter.JAVA_REGEX);
        System.out.println(javaFiles.countLines("/u/a/2014/jromero/Desktop/Untitled Folder"));

   }
}