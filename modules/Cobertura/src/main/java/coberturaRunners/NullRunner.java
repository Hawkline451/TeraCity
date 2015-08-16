package coberturaRunners;
/**
 * Null-Object para Abstract Runner. Si se quiere solo
 * analizar el reporte y asumiendo que este ya esta en
 * la carpeta designada para el reporte del modulo, usar
 * un objeto de esta clase tendria sentido.
 *
 */
public class NullRunner extends AbstractRunner{

	@Override
	protected void compile() {}

	@Override
	protected void instrument() {}

	@Override
	protected void runTests() {}

	@Override
	protected void makeReport() {}

}
