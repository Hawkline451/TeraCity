package coberturaRunners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * 
 * Esta clase asume que el usuario ha corrido Cobertura y generado un reporte.
 * El path del reporte se le entrega al constructor (como String) y se hace una
 * copia local (en REPORTS_PATH) para que esta pueda luego ser an analizada.
 *
 */
public class ReportRunner extends Runner {
	String reportPath;
	public ReportRunner(String report){
		reportPath = report;
	}
	@Override
	protected void compile() {}

	@Override
	protected void instrument() {}

	@Override
	protected void runTests() {}

	@Override
	protected void makeReport() {
		String toPath = BASE + REPORTS_PATH + "/coverage.xml";
		try {
			Files.copy(Paths.get(reportPath), Paths.get(toPath),REPLACE_EXISTING);
		} catch (IOException e) {
			System.out.println("Fallo proceso de copia del reporte");
		}
	}
	@Override
	public void cleanEverythingUp() {
		File datafile = new File(BASE + "/analysis/datafile.ser");
        datafile.delete();
	}
}
