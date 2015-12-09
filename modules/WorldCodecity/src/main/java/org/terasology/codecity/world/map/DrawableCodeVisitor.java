package org.terasology.codecity.world.map;

public interface DrawableCodeVisitor {
	public void visit(DrawableCodeClass code);
	public void visit(DrawableCodePackage code);
}
