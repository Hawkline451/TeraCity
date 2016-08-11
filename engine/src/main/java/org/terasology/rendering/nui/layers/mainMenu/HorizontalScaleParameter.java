package org.terasology.rendering.nui.layers.mainMenu;

import org.terasology.config.RenderingConfig;

public enum HorizontalScaleParameter {
	
	LINELENGTH("class-line-length"){
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
	},
	
	LENGTH("class-length"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	},
	
	COMMENTS("number-of-comments"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	},
	
	IMPORTS("number-of-imports"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	},
	
	METHODCALLS("number-of-method-calls"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	},
	
	METHODS("number-of-methods"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	},
	
	PROP("number-of-properties"){
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
	};
	
	private String horizontalScaleParameterName;

    private HorizontalScaleParameter(String horizontalScaleParameterName) {
        this.horizontalScaleParameterName = horizontalScaleParameterName;
    }

	public abstract void apply(RenderingConfig renderConfig);
	
	@Override
    public String toString() {
        return horizontalScaleParameterName;
    }
}
