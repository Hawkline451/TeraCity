package coberturaRunners;

import java.io.File;
/**
 * Esta familia de clases esta pensada para "correr" Cobertura,
 * es decir, para realizar todos los pasos necesarios para obtener
 * un reporte de Cobertura en relacion a un proyecto. La cantidad
 * y forma de los pasos a realizar para correr Cobertura es dificil 
 * (imposible?) de generalizar completamente, pues dependen completamente
 * del usuario y de la forma en la que su proyecto se organice, asi como
 * de la forma en la que se "buildee" y que tipo de testing use, al mismo
 * tiempo que ya existen varias formas de correr Cobertura (linea de comandos,
 * ant, maven).
 * 
 * Esta clase en especifico provee algunas utilidades varias para Cobertura
 * (las carpetas en el proyecto designadas para contener los varios archivos
 * que correr Cobertura podria originar). Asi como un template con metodos que
 * las clases que la extiendan deben implementar, donde cada metodo dentro del
 * template (runCobertura) corresponde a una de las fases necesarias para 
 * correr Cobertura (compilar ["compile"] no es en si mismo parte de los pasos
 * para correr Cobertura, pero es necesario si los archivos son .java y no .class).
 *
 * Esta familia de clases debe solo correr Cobertura. Parsear o analizar 
 * el reporte en si es una tarea aparte.
 *
 * Cada extension de esta clase puede representar una distinta supocicion 
 * sobre la forma en la que el codigo del usuario esta organizado o los pasos
 * que el usuario ya ha hecho.
 */
public abstract class Runner {
    public static  String BASE = "modules/Cobertura/cobertura-2.1.1";
    public static  String CLASSES_PATH = "/analysis/classes";
    public static  String TEST_CLASSES_PATH = "/analysis/testClasses";
    public static  String INSTRUMENTED_PATH = "/analysis/instrumented";
    public static  String REPORTS_PATH = "/analysis/reports";
    public String pathSep = File.pathSeparator;
    
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
    public abstract void cleanEverythingUp();
    
}
