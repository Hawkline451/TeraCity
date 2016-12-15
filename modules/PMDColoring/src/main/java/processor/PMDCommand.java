package processor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashSet;
import java.util.Hashtable;
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
	
	@Command(shortDescription = "Shows which lines of the specified file have conflicts with the selected rule. "
			+ "If no rule is specified, 'basic' is used by default. Available rules:\n"
			+ "android\n"
			+ "basic\n"
			+ "braces\n"
			+ "clone\n"
			+ "codesize\n"
			+ "comments\n"
			+ "controversial\n"
			+ "coupling\n"
			+ "design\n"
			+ "empty\n"
			+ "finalizers\n"
			+ "imports\n"
			+ "j2ee\n"
			+ "javabeans\n"
			+ "junit\n"
			+ "logging-jakarta-commons\n"
			+ "logging-java\n"
			+ "migrating\n"
			+ "migrating_to_13\n"
			+ "migrating_to_14\n"
			+ "migrating_to_15\n"
			+ "migrating_to_junit4\n"
			+ "naming\n"
			+ "optimizations\n"
			+ "strictexception\n"
			+ "strings\n"
			+ "sunsecure\n"
			+ "typeresolution\n"
			+ "unnecessary\n"
			+ "unusedcode",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String pmdAnalysis(@CommandParam(value = "sourcePath",required = true) String sourcePath,@CommandParam(value="rules",required=false) String rules) throws IOException
    {
    	if (rules == null) rules = "basic";
    	String outPutType = "text";
    	Thread t = new Thread(new ThreadPMDExecution(sourcePath, outPutType, rules,console));
		t.start();
		return "Analysing files with rule: " + rules + "...";
    }
}

class ThreadPMDExecution implements Runnable {
	private String sourcePath;
	private String outPutType;
	private String rules;
	private Console console;
	private String beforePath;
	private String separator;
	

	
	public ThreadPMDExecution(String sourcePath, String outPutType, String rules, Console console) {
		this.sourcePath = sourcePath;
		this.outPutType = outPutType;
		this.rules = rules;
		this.console = console;

		String OS = System.getProperty("os.name");
		if (OS.startsWith("Linux")) {
			this.beforePath = ":";
			this.separator = "/";
		}
		else if (OS.startsWith("Windows")) {
			this.beforePath = "";
			this.separator = "\\";
		}
	}
	
	private static String currentColor;
	public static String getColor() {
		return currentColor;
	}

	private String buildInputString() {
		

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

	private  void saveTSV(Hashtable<String, String> table, String name) throws IOException {
		
		StringBuilder sb = new StringBuilder();
		sb.append(".");
		sb.append(separator);
		sb.append("modules");
		sb.append(separator);
		sb.append("PMDColoring");
		sb.append(separator);
		sb.append("results");
		sb.append(separator);
		sb.append(name);
		sb.append(".tsv");
		
		File file = new File(sb.toString());
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		
		for (String key:table.keySet()) {
			bw.write(key);
			bw.write("\t");
			bw.write(table.get(key));
			bw.write("\n");
		}
		bw.close();
	}
	
	@Override
	public void run() {
		
		String inputString = buildInputString();
		try {
			Process process;
			process = Runtime.getRuntime().exec(inputString);
			InputStream is = process.getInputStream();
			 InputStreamReader isr = new InputStreamReader(is);
			 BufferedReader br = new BufferedReader(isr);
				
			 String line;
			 int messageLines = 0;
			 
			 Hashtable<String, String> table = new Hashtable<String, String>();
			 
			 while ((line = br.readLine()) != null) {
				 line = line.substring(line.lastIndexOf(separator) + 1);
				 String[] occurrence = line.split(":");
				 
				 if (table.containsKey(occurrence[0])) {
					 String value = table.get(occurrence[0]);
					 value = value + "\t" + occurrence[1];
					 table.put(occurrence[0],value);
				 }
				 else {
					 table.put(occurrence[0], occurrence[1]);
				 }
				 
				 ++messageLines;
			 }
			 
			 
			 console.addMessage("Conflictive lines: "+ messageLines);
			 
			 int totalLines = new LineCounter(LineCounter.JAVA_REGEX).countLines(sourcePath);
			
			 console.addMessage("Total lines scanned: "+ totalLines);
			 console.addMessage("Analysis complete");
			 saveTSV(table, rules);
			 
		}
		catch (Exception e) {
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
