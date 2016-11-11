/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.engine;

import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.terasology.config.Config;
import org.terasology.crashreporter.CrashReporter;
import org.terasology.engine.modes.StateInitTeracity;
import org.terasology.engine.modes.StateLoading;
import org.terasology.engine.modes.StateMainMenu;
import org.terasology.engine.paths.PathManager;
import org.terasology.engine.paths.PathSingleton;
import org.terasology.engine.splash.SplashScreen;
import org.terasology.engine.subsystem.EngineSubsystem;
import org.terasology.engine.subsystem.headless.HeadlessAudio;
import org.terasology.engine.subsystem.headless.HeadlessGraphics;
import org.terasology.engine.subsystem.headless.HeadlessInput;
import org.terasology.engine.subsystem.headless.HeadlessTimer;
import org.terasology.engine.subsystem.headless.mode.HeadlessStateChangeListener;
import org.terasology.engine.subsystem.headless.mode.StateHeadlessSetup;
import org.terasology.engine.subsystem.lwjgl.LwjglAudio;
import org.terasology.engine.subsystem.lwjgl.LwjglGraphics;
import org.terasology.engine.subsystem.lwjgl.LwjglInput;
import org.terasology.engine.subsystem.lwjgl.LwjglTimer;
import org.terasology.game.GameManifest;
import org.terasology.network.NetworkMode;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.layers.mainMenu.savedGames.GameInfo;
import org.terasology.rendering.nui.layers.mainMenu.savedGames.GameProvider;

/**
 * Class providing the main() method for launching Terasology as a PC app.
 * <br><br>
 * Through the following launch arguments default locations to store logs and
 * game saves can be overridden, by using the current directory or a specified
 * one as the home directory. Furthermore, Terasology can be launched headless,
 * to save resources while acting as a server or to run in an environment with
 * no graphics, audio or input support. Additional arguments are available to
 * reload the latest game on startup and to disable crash reporting.
 * <br><br>
 * Available launch arguments:
 * <br><br>
 * <table summary="Launch arguments">
 * <tbody>
 * <tr><td>-homedir</td><td>Use the current directory as the home directory.</td></tr>
 * <tr><td>-homedir=path</td><td>Use the specified path as the home directory.</td></tr>
 * <tr><td>-headless</td><td>Start headless.</td></tr>
 * <tr><td>-loadlastgame</td><td>Load the latest game on startup.</td></tr>
 * <tr><td>-noSaveGames</td><td>Disable writing of save games.</td></tr>
 * <tr><td>-noCrashReport</td><td>Disable crash reporting</td></tr>
 * <tr><td>-serverPort=xxxxx</td><td>Change the server port.</td></tr>
 * </tbody>
 * </table>
 * <br><br>
 * When used via command line an usage help and some examples can be obtained via:
 * <br><br>
 * terasology -help    or    terasology /?
 * <br><br>
 *
 * @author Benjamin Glatzel
 * @author Anton Kireev
 * @author and many others
 */


/*AudioInputStream audio = AudioSystem.getAudioInputStream(new File("/home/joaquin/git/TeraCity-master/TeraCity/facades/PC/src/main/java/org/terasology/engine/chewy.wav"));
Clip clip = AudioSystem.getClip();
clip.open(audio);
clip.start();

*/
public final class Terasology {

    private static final String[] PRINT_USAGE_FLAGS = {"--help", "-help", "/help", "-h", "/h", "-?", "/?"};
    private static final String USE_CURRENT_DIR_AS_HOME = "-homedir";
    private static final String USE_SPECIFIED_DIR_AS_HOME = "-homedir=";
    private static final String START_HEADLESS = "-headless";
    private static final String LOAD_LAST_GAME = "-loadlastgame";
    private static final String NO_CRASH_REPORT = "-noCrashReport";
    private static final String NO_SAVE_GAMES = "-noSaveGames";
    private static final String NO_SOUND = "-noSound";
    private static final String SERVER_PORT = "-serverPort=";
    private static final String PROJECT_PATH = "-path=";
    
    private static final String CLEAN_CONFIG = "-cleanConfig";
    private static final String CONFIG_FILE = "config.properties";
    private static final String[] SAVED_PARAMETERS = {
    		USE_SPECIFIED_DIR_AS_HOME, 
    		PROJECT_PATH};

    private static boolean isHeadless;
    private static boolean crashReportEnabled = true;
    private static boolean writeSaveGamesEnabled = true;
    private static boolean soundEnabled = true;
    private static boolean loadLastGame;
    private static String serverPort = null;

    private Terasology() {
    }

    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {

        // To have the splash screen in your favorite IDE add
        //
        //   eclipse:  -splash:src/main/resources/splash.jpg (the PC facade root folder is the working dir.)
        //   IntelliJ: -splash:facades/PC/src/main/resources/splash.jpg (root project is the working dir.)
        //
        // as JVM argument (not program argument!)
    	
    	SplashScreen.getInstance().post("Java Runtime " + System.getProperty("java.version") + " loaded");

        setParametersConfigFile(args);        
        
        handlePrintUsageRequest(args);
        handleLaunchArguments(args);
        
        setupLogging();

        try (final TerasologyEngine engine = new TerasologyEngine(createSubsystemList())) {

            Config config = CoreRegistry.get(Config.class);

            if (!writeSaveGamesEnabled) {
                config.getTransients().setWriteSaveGamesEnabled(writeSaveGamesEnabled);
            }

            if (serverPort != null) {
                config.getTransients().setServerPort(Integer.parseInt(serverPort));
            }

            if (isHeadless) {
                engine.subscribeToStateChange(new HeadlessStateChangeListener());
                engine.run(new StateHeadlessSetup());
            } else {
                SplashScreen.getInstance().close();
                engine.run(new StateInitTeracity());
            }
        } catch (Throwable e) {
            // also catch Errors such as UnsatisfiedLink, NoSuchMethodError, etc.
            SplashScreen.getInstance().close();
            reportException(e);
        }
    }

    private static void setupLogging() {
        Path path = PathManager.getInstance().getLogPath();
        if (path == null) {
            path = Paths.get("logs");
        }

        LoggingContext.initialize(path);
    }

    private static void handlePrintUsageRequest(String[] args) {
        for (String arg : args) {
            for (String usageArg : PRINT_USAGE_FLAGS) {
                if (usageArg.equals(arg.toLowerCase())) {
                    printUsageAndExit();
                }
            }
        }
    }

    private static void printUsageAndExit() {

        String printUsageFlags = Joiner.on("|").join(PRINT_USAGE_FLAGS);

        List<String> opts = ImmutableList.of(
                printUsageFlags,
                USE_CURRENT_DIR_AS_HOME + "|" + USE_SPECIFIED_DIR_AS_HOME + "<path>",
                START_HEADLESS,
                LOAD_LAST_GAME,
                NO_CRASH_REPORT,
                NO_SAVE_GAMES,
                NO_SOUND,
                SERVER_PORT + "<port>");

        StringBuilder optText = new StringBuilder();

        for (String opt : opts) {
            optText.append(" [" + opt + "]");
        }

        System.out.println("Usage:");
        System.out.println();
        System.out.println("    terasology" + optText.toString());
        System.out.println();
        System.out.println("By default Terasology saves data such as game saves and logs into subfolders of a platform-specific \"home directory\".");
        System.out.println("Saving can be explicitly disabled using the \"" + NO_SAVE_GAMES + "\" flag.");
        System.out.println("Optionally, the user can override the default by using one of the following launch arguments:");
        System.out.println();
        System.out.println("    " + USE_CURRENT_DIR_AS_HOME + "        Use the current directory as the home directory.");
        System.out.println("    " + USE_SPECIFIED_DIR_AS_HOME + "<path> Use the specified directory as the home directory.");
        System.out.println();
        System.out.println("It is also possible to start Terasology in headless mode (no graphics), i.e. to act as a server.");
        System.out.println("For this purpose use the " + START_HEADLESS + " launch argument.");
        System.out.println();
        System.out.println("To automatically load the latest game on startup,");
        System.out.println("use the " + LOAD_LAST_GAME + " launch argument.");
        System.out.println();
        System.out.println("By default Crash Reporting is enabled.");
        System.out.println("To disable this feature use the " + NO_CRASH_REPORT + " launch argument.");
        System.out.println();
        System.out.println("To disable sound use the " + NO_SOUND + " launch argument (default in headless mode).");
        System.out.println();
        System.out.println("To change the port the server is hosted on use the " + SERVER_PORT + " launch argument.");
        System.out.println();
        System.out.println("To reset the config file ("+ PROJECT_PATH + " and "+ USE_SPECIFIED_DIR_AS_HOME +") use the " + CLEAN_CONFIG  + " launch argument.");
        System.out.println();
        System.out.println("Examples:");
        System.out.println();
        System.out.println("    Use the current directory as the home directory:");
        System.out.println("    terasology " + USE_CURRENT_DIR_AS_HOME);
        System.out.println();
        System.out.println("    Use \"myPath\" as the home directory:");
        System.out.println("    terasology " + USE_SPECIFIED_DIR_AS_HOME + "myPath");
        System.out.println();
        System.out.println("    Start terasology in headless mode (no graphics) and enforce using the default port:");
        System.out.println("    terasology " + START_HEADLESS + " " + SERVER_PORT + TerasologyConstants.DEFAULT_PORT);
        System.out.println();
        System.out.println("    Load the latest game on startup and disable crash reporting");
        System.out.println("    terasology " + LOAD_LAST_GAME + " " + NO_CRASH_REPORT);
        System.out.println();
        System.out.println("    Don't start Terasology, just print this help:");
        System.out.println("    terasology " + PRINT_USAGE_FLAGS[1]);
        System.out.println();

        System.exit(0);
    }

    private static void handleLaunchArguments(String[] args) {
    	Properties prop = new Properties();
    	InputStream input = null;
    	
    	String DIR_AS_HOME_CONFIG_FILE = "";
    	String PROJECT_PATH_CONFIG_FILE = "";
    	
    	try {
    		input = new FileInputStream(CONFIG_FILE);
    		prop.load(input);
    		DIR_AS_HOME_CONFIG_FILE = prop.getProperty(USE_SPECIFIED_DIR_AS_HOME);
    		PROJECT_PATH_CONFIG_FILE = prop.getProperty(PROJECT_PATH);

    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}   	
    	
        Path homePath = null;
        Path projectPath = null;
        
        boolean readHomeConfigFile = true;
        boolean readPathConfigFile = true;
        
        for (String arg : args) {
            boolean recognized = true;
            if (arg.startsWith(USE_SPECIFIED_DIR_AS_HOME)) {
            	homePath = Paths.get(arg.substring(USE_SPECIFIED_DIR_AS_HOME.length()));
            	readHomeConfigFile = false;
            } else if (arg.equals(USE_CURRENT_DIR_AS_HOME)) {
                homePath = Paths.get("");
            } else if(arg.startsWith(PROJECT_PATH)){
            	projectPath = Paths.get(arg.substring(PROJECT_PATH.length()));
            	readPathConfigFile = false;
            } else if (arg.equals(START_HEADLESS)) {
                isHeadless = true;
                crashReportEnabled = false;
            } else if (arg.equals(NO_SAVE_GAMES)) {
                writeSaveGamesEnabled = false;
            } else if (arg.equals(NO_CRASH_REPORT)) {
                crashReportEnabled = false;
            } else if (arg.equals(NO_SOUND)) {
                soundEnabled = false;
            } else if (arg.equals(LOAD_LAST_GAME)) {
                loadLastGame = true;
            } else if (arg.startsWith(SERVER_PORT)) {
                serverPort = arg.substring(SERVER_PORT.length());
            } else if (arg.equals(CLEAN_CONFIG)){
            	cleanParametersConfigFile();
            	readHomeConfigFile = false;
            	readPathConfigFile = false;
            	homePath = null;
            	projectPath = null;
            }else {
                recognized = false;
            }

            System.out.println((recognized ? "Recognized" : "Invalid") + " argument: " + arg);
        }
        
        if(readHomeConfigFile && !DIR_AS_HOME_CONFIG_FILE.equals("")){
        	homePath = Paths.get(DIR_AS_HOME_CONFIG_FILE);
        }
        if(readPathConfigFile && !PROJECT_PATH_CONFIG_FILE.equals("")){
        	projectPath = Paths.get(PROJECT_PATH_CONFIG_FILE);
        }
        
        try {
        	if (projectPath != null){
        		PathManager.getInstance().setWorldPath(projectPath);
        	}
            if (homePath != null) {
                PathManager.getInstance().useOverrideHomePath(homePath);
            } else {
                PathManager.getInstance().useDefaultHomePath();
            }

        } catch (IOException e) {
            reportException(e);
            System.exit(0);
        }
    }

    private static Collection<EngineSubsystem> createSubsystemList() {
        if (isHeadless) {
            return Lists.newArrayList(new HeadlessGraphics(), new HeadlessTimer(), new HeadlessAudio(), new HeadlessInput());
        } else {
            EngineSubsystem audio = soundEnabled ? new LwjglAudio() : new HeadlessAudio();
            return Lists.<EngineSubsystem>newArrayList(new LwjglGraphics(), new LwjglTimer(), audio, new LwjglInput());
        }
    }

    private static void reportException(Throwable throwable) {
        Path logPath = Paths.get(".");
        try {
            Path gameLogPath = PathManager.getInstance().getLogPath();
            if (gameLogPath != null) {
                logPath = gameLogPath;
            }
        } catch (Exception pathManagerConstructorFailure) {
            throwable.addSuppressed(pathManagerConstructorFailure);
        }

        if (!GraphicsEnvironment.isHeadless() && crashReportEnabled) {
            CrashReporter.report(throwable, logPath);
        } else {
            throwable.printStackTrace();
            System.err.println("For more details, see the log files in " + logPath.toAbsolutePath().normalize());
        }
    }

    private static GameManifest getLatestGameManifest() {
        GameInfo latestGame = null;
        List<GameInfo> savedGames = GameProvider.getSavedGames();
        for (GameInfo savedGame : savedGames) {
            if (latestGame == null || savedGame.getTimestamp().after(latestGame.getTimestamp())) {
                latestGame = savedGame;
            }
        }

        if (latestGame == null) {
            return null;
        }

        return latestGame.getManifest();
    }

    /**
     * Return if parameter is a saved parameter
     * @param parameter
     * @return boolean
     */      
    private static boolean isSavedParameter(String param){
    	for (String p: SAVED_PARAMETERS) {
    		if(param.equals(p)){
    			return true;
    		}
    	}
    	return false;
    }
    
    
    /**
     * Set parameters in config file.
	 * @param args
     */   
    private static void setParametersConfigFile(String[] args){
    	
    	Properties props = new Properties();
    	
    	FileInputStream input = null;
    	OutputStream output = null;
    	
    	try {
    		
    		File fl = new File(CONFIG_FILE);
    		if(!fl.exists()) {
    		    fl.createNewFile();
    		} 
    		
    		input = new FileInputStream(CONFIG_FILE);
    		props.load(input);
    		input.close();
    		
    		output = new FileOutputStream(CONFIG_FILE);
    		// If File config doesn't exist
    		for (String p: SAVED_PARAMETERS){
    			String paramv = props.getProperty(p);
    			System.out.println(paramv);
    			if(paramv == null){
    				props.setProperty(p, "");
    			}
    		}
    		
    		// Set parameters in args
            for (String arg : args) {
            	for(String p: SAVED_PARAMETERS){
	               	 if (arg.startsWith(p)) {
	             		props.setProperty(p, arg.substring(p.length()));
	             	}
            	}
            }
    		props.store(output, null);    		
    	} catch (IOException io) {
    		io.printStackTrace();
    	} finally {
    		if (input != null) {
    			try {
    				input.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		if (output != null) {
    			try {
    				output.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
   
    /**
     * Reset parameters in config file.
     */   
    private static void cleanParametersConfigFile(){
    	Properties props = new Properties();
    	OutputStream output = null;
    	try {
    		output = new FileOutputStream(CONFIG_FILE);
    		
    		for (String p: SAVED_PARAMETERS){
    			props.setProperty(p, "");
    		}
    		props.store(output, null);    		
    	} catch (IOException io) {
    		io.printStackTrace();
    	} finally {
    		if (output != null) {
    			try {
    				output.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }
    
    
}
