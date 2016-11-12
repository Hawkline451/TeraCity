package org.terasology.codecity.world.glasswall.coloringAPI;

public class TextSearchQuery extends AbstractColoringQuery {
  private String search;
  
  /**
   * TextSearchQuery(String)
   * Initializes the query that searches for the string in search.
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
