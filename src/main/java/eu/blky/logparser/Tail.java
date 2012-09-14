package eu.blky.logparser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Java implementation of the Unix tail command
 * 
 * @param args[0] File name
 * @param args[1] Update time (seconds). Optional. Default value is 1 second
 * 
 * @author Luigi Viggiano (original author) http://it.newinstance.it/2005/11/19/listening-changes-on-a-text-file-unix-tail-implementation-with-java/
 * @author Alessandro Melandri (modified by)
 * */
public class Tail {

  static long sleepTime = 1000;

  public static void main(String[] args) throws IOException {

    if (args.length > 0){

      if (args.length > 1)
        sleepTime = Long.parseLong(args[1]) * 1000;

      BufferedReader input = new BufferedReader(new FileReader(args[0]));
      String currentLine = null;

      while (true) {

        if ((currentLine = input.readLine()) != null) {
          System.out.println(currentLine);
          continue;
        }

        try {
          Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }

      }
      input.close();

    } else {
      System.out.println("Missing parameter!\nUsage: java JavaTail fileName [updateTime (Seconds. default to 1 second)]");
    }
  }
}