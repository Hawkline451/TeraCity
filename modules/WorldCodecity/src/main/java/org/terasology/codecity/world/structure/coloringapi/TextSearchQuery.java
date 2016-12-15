package org.terasology.codecity.world.structure.coloringapi;
import org.terasology.world.block.Block;

public class TextSearchQuery extends AbstractColoringQuery {
  
  /**
   * TextSearchQuery(String)
   * Initializes the query that searches for the string in the variable search.
   * @param search
   */
  public TextSearchQuery(String search) {
    this.search = search;
  }

  /**
   * 
   */
  @Override
  public GlassColor giveLineColor(int y) {
    
    return null;
  }
  
  
}
