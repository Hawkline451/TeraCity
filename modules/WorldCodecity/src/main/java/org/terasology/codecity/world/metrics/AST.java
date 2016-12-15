package org.terasology.codecity.world.metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AST extends VoidVisitorAdapter<Object> {

	private ArrayList<MethodCallExpr> methodCalls;
	private List<Comment> comments;
	private List<ImportDeclaration> imports;
	private List<FieldDeclaration> fields;
	private PackageDeclaration pack;
	private ArrayList<MethodDeclaration> methods;
	private String path;
	private int length;
	private int[] linesLength;
	private File file;
	private CompilationUnit cu;

	public AST(String location) {
		super();
		path = location;
		methodCalls = new ArrayList<MethodCallExpr>();
		fields = new ArrayList<FieldDeclaration>();
		comments = new ArrayList<Comment>();
		imports = new ArrayList<ImportDeclaration>();
		methods = new ArrayList<MethodDeclaration>();
		file = new File(path);
		try {
			cu = JavaParser.parse(file);
			this.setGeneralMetrics(cu);
			this.visit(cu, null);
		} catch (ParseException | IOException e) {
			new RuntimeException(e);
		}
	}

	@Override
	public void visit(MethodCallExpr n, Object arg) {
		super.visit(n, arg);
		methodCalls.add(n);
	}

	@Override
	public void visit(MethodDeclaration n, Object arg) {
		super.visit(n, arg);
		methods.add(n);
	}

	/**
	 * @return Representation of Code
	 */
	public CompilationUnit getCu() {
		return cu;
	}
	
	/**
	 * @param text String to search
	 * @return if .java contains indicated text
	 */
	public boolean contains(String text) {
		return cu.toString().contains(text);
	}
	
	public boolean[] containsLines(String text) {
	  String[] lines = cu.toString().split("\n");
	  boolean[] foundrows = new boolean[lines.length];
	  for (int i = 0; i < foundrows.length; i++) {
	    foundrows[i] = lines[i].contains(text);
	  }
	  return foundrows;
	}

	/**
	 * @return Methods calls made in code
	 */
	public ArrayList<MethodCallExpr> getMethodCalls() {
		return methodCalls;
	}
	
	public ArrayList<MethodDeclaration> getMethods() {
		return methods;
	}

	/**
	 * Sets general metrics for the class, such as
	 * length, comments, packages, importations, etc
	 * @param cu
	 */
	private void setGeneralMetrics(CompilationUnit cu) {
		length = cu.getEndLine();
		comments = (cu.getComments()!=null) ? cu.getComments() : comments;
		imports = (cu.getImports()!=null) ? cu.getImports() : imports;
		pack = cu.getPackage();
		linesLength = countLineLength();
		for (TypeDeclaration typeDec : cu.getTypes()) {
			List<BodyDeclaration> members = typeDec.getMembers();
			if (members != null) {
				for (BodyDeclaration member : members) {
					try {
						FieldDeclaration field = (FieldDeclaration) member;
						fields.add(field);
					} catch (ClassCastException e) {

					}
				}
			}
		}
	}

	/**
	 * Counts length for every line in code
	 * @return Array with lengths
	 */
	private int[] countLineLength() {
		BufferedReader br;
		int[] resp = null;
		try {
			br = new BufferedReader(new FileReader(path));
			String nl;
			resp = new int[length];
			int i = 0;
			while ((nl = br.readLine()) != null) {
				resp[i] = nl.length();
				i++;
			}
			br.close();
		} catch (IOException e) {
			return resp;
		} 
		return resp;
	}

	/**
	 * @return Length of code
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return ArrayList with comments included in code
	 */
	public List<Comment> getComments() {
		return comments;
	}

	/**
	 * @return ArrayList with imports made in code
	 */
	public List<ImportDeclaration> getImports() {
		return imports;
	}

	/**
	 * @return Package that contains code
	 */
	public PackageDeclaration getPackage() {
		return pack;
	}

	/**
	 * @return Variables/Fields of code
	 */
	public List<FieldDeclaration> getFields() {
		return fields;
	}

	/**
	 * @return Int[] with length of lines in the codes magic!
	 */
	public int[] getLinesLength() {
		return linesLength;
	}
}
