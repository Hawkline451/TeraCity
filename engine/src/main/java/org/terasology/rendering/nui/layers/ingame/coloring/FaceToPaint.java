package org.terasology.rendering.nui.layers.ingame.coloring;

import org.terasology.config.RenderingConfig;

public enum FaceToPaint {
	ALL("all") {
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
    },
    NORTH("north") {
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
    },
    EAST("east") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    SOUTH("south") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    WEST("west") {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    };

    private String faceName;

    private FaceToPaint(String faceName) {
        this.faceName = faceName;
    }

    public abstract void apply(RenderingConfig renderConfig);

    @Override
    public String toString() {
        return faceName;
    }
}
