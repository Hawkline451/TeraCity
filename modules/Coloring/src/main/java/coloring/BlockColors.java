package coloring;

public enum BlockColors {

	BLOCK_BLUE("blue"),
	BLOCK_GREEN("green"),
	BLOCK_ORANGE("orange"),
	BLOCK_RED("red"),
	BLOCK_YELLOW("yellow"),
	BLOCK_NOTFOUND("notfound"),
	BLOCK_TRANSP_BLUE("transparentBlue"),;
	
	private final String text;
	
	private BlockColors(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
