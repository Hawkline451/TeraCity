package org.terasology.codecity.world.structure.coloringapi;

/**
 * @author carlos_meneses
 * Class that saves the query to be used to paint the glass walls and returns
 * the color of each block of a glass wall as a double array.
 */
public class GlassWallColoringAPI {
  //the criterion under which the colors of the GlassWall blocks are colored
  public AbstractColoringQuery query;
  
  /*
  *setCoverageColoringQuery()
  *sets the actual query to color in function of test coverage
  */
  public void setCoverageQuery() {
    query = new CoverageQuery();
  }
  
  /*
   *setTextSearchQuery()
   *sets the actual query to color in function of the search for a string
   */
  public void setTextSearchQuery(String search) {
    query = new TextSearchQuery(search);
  }
  
  /*
   *setProblemQuery()
   *sets the actual query to color in function of which lines of code have
   *problems
   */
  public void setProblemQuery() {
    query = new ProblemQuery();
  }
}
