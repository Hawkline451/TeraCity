package org.terasology.codecity.world.structure.coloringapi;

import org.terasology.world.block.Block;

public abstract class AbstractColoringQuery implements ColoringQueryInterface {
  Block trueblock;
  Block falseblock;
  String search;
}
