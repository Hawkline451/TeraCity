package org.terasology.codecity.world.structure.scale;

public class HalfLinearCodeScale implements CodeScale {

    @Override
    public int getScaledSize(int size) {
        return size/2;
    }

    @Override
    public int getScaledSize(int size, int min) {
        return Math.max(getScaledSize(size), min);
    }

}
