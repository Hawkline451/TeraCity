package org.terasology.codecity.world.structure.scale;

/**
 * This class represent the scale applied to the code to show it in a better
 * way.
 */
public abstract class CodeScale {
    /**
     * Get the scaled size of a number
     * 
     * @param size
     *            Number to be scaled
     * @return The scaled version of the number
     */
    public abstract int getScaledSize(int size);

    /**
     * Get the scaled size of a number
     * 
     * @param size
     *            Number to be scaled
     * @param min
     *            Minimal value
     * @return The scaled version of the number
     */
    public int getScaledSize(int size, int min) {
        return Math.max(getScaledSize(size), min);
    }
}
