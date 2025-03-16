package toolbox;

public class LoggingHelper {
    public static void log(String message) {
        System.out.println(DateTimeStamp.time() + ": " + message);
    }
}
