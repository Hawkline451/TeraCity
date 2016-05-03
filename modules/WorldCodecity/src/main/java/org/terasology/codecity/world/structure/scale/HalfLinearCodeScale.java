package org.terasology.codecity.world.structure.scale;

public class HalfLinearCodeScale extends CodeScale {

    @Override
    public int getScaledSize(int size) {
        return size/2;
    }

}
