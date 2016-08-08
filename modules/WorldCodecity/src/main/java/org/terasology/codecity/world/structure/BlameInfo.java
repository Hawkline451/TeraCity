package org.terasology.codecity.world.structure;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Huan
 * Stores information of each line of code
 */
public class BlameInfo {
  String author;
  Date time;
  String tz;

  public BlameInfo(String aut, String tm, String z) {
    author = aut;
    time = new Date((long) (TimeUnit.SECONDS.toMillis(Integer.parseInt(tm))));
    tz = z;
  }

  public String getAuthor() {
    return author;
  }

  public Date getTime() {
    return time;
  }

  public String getTimezone() {
    return tz;
  }
  
  public String toString() {
    return author + " " + time + " " + tz;
  }
}
