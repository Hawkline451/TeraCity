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
	private String path;
	private int length;
	private int[] linesLength;
	private File file;

	public AST(String location) {
		super();
		path = location;
		methodCalls = new ArrayList<MethodCallExpr>();
		fields = new ArrayList<FieldDeclaration>();
		file = new File(path);
		try {
			CompilationUnit cu = JavaParser.parse(file);
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
	}

	public ArrayList<MethodCallExpr> getMethodCalls() {
		return methodCalls;
	}

	private void setGeneralMetrics(CompilationUnit cu) {
		length = cu.getEndLine();
		comments = cu.getComments();
		imports = cu.getImports();
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

	public int getLength() {
		return length;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public List<ImportDeclaration> getImports() {
		return imports;
	}

	public PackageDeclaration getPackage() {
		return pack;
	}

	public List<FieldDeclaration> getFields() {
		return fields;
	}

	public int[] getLinesLength() {
		return linesLength;
	}
}