package org.terasology.rendering.nui.layers.mainMenu;

import org.terasology.config.RenderingConfig;

public enum VerticalScaleType {
	
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
	
	private String verticalScaleTypeName;

    private VerticalScaleType(String verticalScaleTypeName) {
        this.verticalScaleTypeName = verticalScaleTypeName;
    }

	public abstract void apply(RenderingConfig renderConfig);
	
	@Override
    public String toString() {
        return verticalScaleTypeName;
    }
	
}
