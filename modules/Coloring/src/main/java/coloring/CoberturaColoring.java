package coloring;

import java.io.IOException;

import commands.CoberturaCommand;

public class CoberturaColoring extends AbstractColoring{
	CoberturaCommand cobby;
	
	public CoberturaColoring(){
		cobby = new CoberturaCommand();
	}
	
	@Override
	public String getColor(String path) {
		return CoberturaCommand.getColor(path);
	}
	@Override
	public void getDataColoring() throws IOException {
		cobby.analyze(params[0], params[1], params[2]);
		cobby.waitForAnalysis();
	}
}
