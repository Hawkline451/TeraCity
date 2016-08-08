package org.terasology.codecity.world.structure.metric;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.terasology.codecity.world.structure.BlameInfo;

/**
 * Creates a metric that associates each line with its author
 * and last modification date
 * @author Huan
 *
 */
public class GitBlameMetric extends CodeMetric {

  private Map<Integer, BlameInfo> blames = new HashMap<Integer, BlameInfo>();

  
  public GitBlameMetric(String path) {
    try {
      createBlame(path);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  

  /**
   * Adds the information of author and last modification to each line of code.
   * 
   * @param path
   *          Path to the file that's being analysed.
   * @throws IOException
   */
  private void createBlame(String path) throws IOException {
    if (path != null && (new File(path).isFile())) {
      String os = System.getProperty("os.name");
      ProcessBuilder pb = null;
      if (os.startsWith("Windows")) {
        pb = new ProcessBuilder("git.exe", "blame", "-p", path);
      } else {
        // :DDDDDDDD
      }

      if (pb != null) {
        pb.redirectErrorStream(true);
        Process pr = pb.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            pr.getInputStream()));
        String line;

        Integer number = null;
        String author = "No information";
        String time = "0";
        String tz = "-0000";
        boolean first = true;
        Pattern p = Pattern.compile("^[a-z0-9]*$");
        while ((line = in.readLine()) != null) {
          String[] ln = parseBlameLine(line, p);
          if (ln[0].equals("line_number")) {
            if (!first) {
              blames.put(number, new BlameInfo(author, time, tz));
            }
            first = false;
            number = Integer.parseInt(ln[1]);
          } else if (ln[0].equals("author")) {
            author = ln[1];
          } else if (ln[0].equals("author-time")) {
            time = ln[1];
          } else if (ln[0].equals("author-tz")) {
            tz = ln[1];
          }

        }
        if (!first) {
          blames.put(number, new BlameInfo(author, time, tz));

        }
      }

    }

  }

  /**
   * Takes a read line and parses into a format of [parameter, information] if
   * the information is useful.
   * 
   * @param line
   *          line to parse
   * @param p
   *          Pattern matcher for the ID line
   * @return
   */
  private String[] parseBlameLine(String line, Pattern p) {
    line = line.trim();
    if (line != null) {
      String[] seps = line.split(" ");
      if (seps[0].length() == 40 && p.matcher(seps[0]).matches()) {
        return new String[] { "line_number", seps[2] };
      } else if (seps[0].equals("author")) {
        seps[0] = "";
        StringBuilder builder = new StringBuilder();
        for (String s : seps) {
          builder.append(s);
          builder.append(" ");
        }
        return new String[] { "author", builder.toString().trim() };
      } else if (seps[0].equals("author-time")) {
        return new String[] { "author-time", seps[1] };
      } else if (seps[0].equals("author-tz")) {
        return new String[] { "author-tz", seps[1] };
      }
    }
    return new String[] { "no match", "no info" };
  }

  
  public BlameInfo getLineInfo(int i) {
    if (blames.containsKey(i)) {
      return blames.get(i);
    }
    return new BlameInfo("No information", "0", "-0000");
  }

  
}
