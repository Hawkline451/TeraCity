package org.terasology.codecity.world.structure.scale;

/**
 * This class implement a scale using a Square root
 */
public class SquareRootCodeScale extends CodeScale {

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScaledSize(int size) {
        return (int) Math.sqrt(size);
    }
}
