package coberturaRunners;

import java.io.File;
// TODO Docs of the whole hierarchy.
public abstract class AbstractRunner {
    protected static final String BASE = "modules/Cobertura/cobertura-2.1.1";
    protected static final String CLASSES_PATH = "/analysis/classes";
    protected static final String TEST_CLASSES_PATH = "/analysis/testClasses";
    protected static final String INSTRUMENTED_PATH = "/analysis/instrumented";
    protected static final String REPORTS_PATH = "/analysis/reports";
    protected String pathSep = File.pathSeparator;
    
    public void runCobertura(){
    	compile();
    	instrument();
    	runTests();
    	makeReport();
    }
    protected abstract void compile();
    protected abstract void instrument();
    protected abstract void runTests();
    protected abstract void makeReport();
    
}
