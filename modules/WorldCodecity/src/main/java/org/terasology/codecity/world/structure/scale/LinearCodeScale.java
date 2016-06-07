package org.terasology.codecity.world.structure.scale;

public class LinearCodeScale extends CodeScale {

    @Override
    public int getScaledSize(int size) {
        return (int) Math.sqrt(size);
    }

}
