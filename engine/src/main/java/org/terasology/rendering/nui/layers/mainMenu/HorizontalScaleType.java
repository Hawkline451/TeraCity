package org.terasology.rendering.nui.layers.mainMenu;

import org.terasology.config.RenderingConfig;

public enum HorizontalScaleType {
	LINEAR("linear"){
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
	},
	
	HALFLINEAR("half-linear"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	},
	
	SQRT("square-root"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	};
	
	private String horizontalScaleTypeName;

    private HorizontalScaleType(String horizontalScaleTypeName) {
        this.horizontalScaleTypeName = horizontalScaleTypeName;
    }

	public abstract void apply(RenderingConfig renderConfig);
	
	@Override
    public String toString() {
        return horizontalScaleTypeName;
    }


}
