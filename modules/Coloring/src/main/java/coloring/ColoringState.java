package coloring;

public class ColoringState {

	private boolean renderQuakes;
	public static final int MAX_HEALTH = 12;
	
	public ColoringState() {
		renderQuakes = true;
	}
	
	public boolean renderQuakes() {
		return this.renderQuakes;
	}
	
	public void setRenderQuakes(boolean value) {
		this.renderQuakes = value;
	}
	
}
