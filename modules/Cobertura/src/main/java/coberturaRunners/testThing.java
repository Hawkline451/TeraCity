package coberturaRunners;

import commands.CoberturaCommand;

public class testThing {

	public static void main(String[] args) {
//		String path = "C:/Users/andres/git/PacmanTest/MyPacman";
//		String path = "C:/Users/andres/workspace/TeraCity/modules/WorldCodecity";
		String path = "C:/Users/andres/git/PacmanTest/MyPacman/bin/tests";
		path = "C:/Users/andres/Desktop/logisim-master";
		String testPath = "C:/Users/andres/git/PacmanTest/MyPacman/bin/tests";
		CLSingleFolderRunner da = new CLSingleFolderRunner(path);
		da.runCobertura();
		
//		CLTwoFoldersRunner tes = new CLTwoFoldersRunner(path, testPath);
//		tes.runCobertura();
		
//		
//		CoberturaCommand d2 = new CoberturaCommand();
//		d2.analyze("-s", path, testPath);
//		d2.getMetricValue(testPath);
//		

	}

}
