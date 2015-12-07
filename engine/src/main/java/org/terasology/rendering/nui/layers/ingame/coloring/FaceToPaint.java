package org.terasology.rendering.nui.layers.ingame.coloring;

import org.terasology.config.RenderingConfig;
import org.terasology.world.block.BlockPart;

public enum FaceToPaint {
	ALL("all", BlockPart.CENTER) {
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
    },
    NORTH("north", BlockPart.BACK) {
		@Override
		public void apply(RenderingConfig renderConfig) {
		}
    },
    EAST("east", BlockPart.RIGHT) {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    SOUTH("south", BlockPart.FRONT) {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    },
    WEST("west", BlockPart.LEFT) {
        @Override
        public void apply(RenderingConfig renderConfig) {
        }
    };

    private String faceName;
    private BlockPart part;

    private FaceToPaint(String faceName, BlockPart part) {
        this.faceName = faceName;
        this.part     = part;
    }

    public abstract void apply(RenderingConfig renderConfig);

    public BlockPart getBlockPart() {
    	return part;
    }
    
    public static FaceToPaint fromString(String face) {
    	if (face.equals("all")) {
    		return FaceToPaint.ALL;
    	}
    	if (face.equals("east")) {
    		return FaceToPaint.EAST;
    	}
    	if (face.equals("west")) {
    		return FaceToPaint.WEST;
    	}
    	if (face.equals("north")) {
    		return FaceToPaint.NORTH;
    	}
    	if (face.equals("south")) {
    		return FaceToPaint.SOUTH;
    	}
    	return null;
    }
    
    @Override
    public String toString() {
        return faceName;
    }
}
