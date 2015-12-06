package org.terasology.rendering.nui.layers.ingame.coloring;

import org.terasology.config.RenderingConfig;

public enum ColorScale {
	RAINBOW("rainbow") {
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
    },
    RED("red") {
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
    },
    GREEN("green") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    YELLOW("yellow") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    BLUE("blue") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    ORANGE("orange") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    };

    private String colorName;

    private ColorScale(String colorName) {
        this.colorName = colorName;
    }

    public abstract void apply(RenderingConfig renderConfig);
        
    @Override
    public String toString() {
        return colorName;
    }
}
