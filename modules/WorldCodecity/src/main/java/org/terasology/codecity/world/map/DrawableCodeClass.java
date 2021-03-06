package org.terasology.codecity.world.map;

import org.terasology.codecity.world.metrics.AST;
import org.terasology.codecity.world.structure.CodeClass;
import org.terasology.codecity.world.structure.metric.CodeMetricManager;
import org.terasology.codecity.world.structure.scale.CodeScaleManager;
import org.terasology.registry.CoreRegistry;

public class DrawableCodeClass implements DrawableCode {
    private CodeClass base;

    /**
     * This class represent a Class that can be drawed in the map
     * 
     * @param codeClass
     *            Base CodeClass
     */
    public DrawableCodeClass(CodeClass codeClass) {
        base = codeClass;
    }
    
    /**
     * Get the CodeClass which is the base of the drawable representation.
     * @return base of the DrawableCodeClass class.
     */
    public CodeClass getBase() {
        return base;
    }
    
    //TODO factory is not being used in any of the gets
    @Override
    public int getSize(CodeMapFactory factory) {
        CodeScaleManager man = CoreRegistry.get(CodeScaleManager.class);
        CodeMetricManager men = CoreRegistry.get(CodeMetricManager.class);
        return man.getHorizontalScale().getScaledSize(Integer.parseInt(men.getHorizontalMetric().getMetricVal(base)), 1);
    }

    @Override
    public int getHeight(CodeMapFactory factory) {
    	CodeScaleManager man = CoreRegistry.get(CodeScaleManager.class);
        CodeMetricManager men = CoreRegistry.get(CodeMetricManager.class);
        return man.getVerticalScale().getScaledSize(Integer.parseInt(men.getVerticalMetric().getMetricVal(base)), 1);
    }

    @Override
    public CodeMap getSubmap(CodeMapFactory factory) {
        return new CodeMapNull();
    }

	@Override
	public int getWidth(CodeMapFactory factory) {
		CodeScaleManager man = CoreRegistry.get(CodeScaleManager.class);
		return man.getHorizontalScale().getScaledSize(base.getLongestLineLength(), 1);
	}

	@Override
	public boolean containsClass(String className) {
		return base.getName().equals(className);
	}

	@Override
	public void accept(DrawableCodeVisitor visitor) {
		visitor.visit(this);
	}
	@Override
	public int[] getLineLength() {
		return getBase().getLineLengths();
	}

	@Override
	public int[][] getLowResFromLine(int row,int column) {
		return getBase().getBinaryRow(row,column);
	}

	@Override
	public int[][] getFullRep() {
		// TODO Auto-generated method stub
		return getBase().getFullBinary();
	}

	@Override
	public boolean useTexture() {
		return true;
	}

	@Override
	public boolean containsText(String query) {
		AST ast = base.getAst();
		return ast.contains(query);
	}

	@Override
	public boolean containsPackage(String asterix) {
		AST ast = base.getAst();
		String pack = ast.getPackage().toString().trim().split(" ")[1].trim();
		return pack.substring(0, pack.length()).equals(asterix);
	}
}
