package utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataWarning{
	int line;
	int find;
	int max;
	String color;

	public DataWarning(int line, int find, int max) {
		this.line = line;
		this.find = find;
		this.max = max;
		generateColor();
	}

	public DataWarning(int line, String message) {
		Pattern regExpr = Pattern.compile(".* (\\d+).*\\(.* (\\d+).*");
		String str = message;
		Matcher matcher = regExpr.matcher(str);
		matcher.find();
		find = Integer.parseInt(matcher.group(1));
		max = Integer.parseInt(matcher.group(2));
		this.line = line;
		generateColor();
	}
	
	public int getLine() {
		return line;
	}
	
	private void generateColor() {
		color = "Red";
	}
	
	public String getColor() {
		return color;
	}
	
	public String toString() {
		return "(linea: " + line + ", encontrado: " + find + ", max: " + max + ")";
	}
}
