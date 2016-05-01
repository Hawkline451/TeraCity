package org.terasology.world.block;

import java.awt.image.BufferedImage;
import java.util.HashMap;

/*
 * Class to generate the blocks' image.
 * 
 */
public final class BlockTextureFactory {

  private static BlockTextureFactory fact;
  private HashMap<String, BufferedImage> pics;

  private BlockTextureFactory() {
    pics = new HashMap<String, BufferedImage>();
  }

  public static BlockTextureFactory getFactory() {
    if (fact == null)
      fact = new BlockTextureFactory();
    return fact;
  }
  

  public BufferedImage getImage(int line1, int line2) {
    return getImage(line1,line2,1,1);
  }

  /**
   * Returns the image for the block stored in the map, if it doesn't exist it's
   * created
   * 
   * @param line1
   *          Length of the first line of code
   * @param line2
   *          Length of the second line
   * @param pos
   *          Position of the texture in the building if the width is bigger
   *          than one.
   * @param total
   *          Width of the building
   * 
   * @return Texture image
   */
  public BufferedImage getImage(int line1, int line2, int pos, int total) {
    return null;
  }
}
