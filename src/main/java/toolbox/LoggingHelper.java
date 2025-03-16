package toolbox;

/**
 * Utility class for logging application messages.
 */
public class LoggingHelper {

    /**
     * Logs an informational message with a timestamp.
     *
     * @param message The message to be logged.
     */
    public static void log(String message) {
        System.out.println(DateTimeStamp.time() + ": " + message);
    }

    /**
     * Formats a message with the current timestamp.
     *
     * @param message The original message.
     * @return A formatted string with a timestamp.
     */
    public static String formatMessage(String message) {
        return DateTimeStamp.time() + ": " + message;
    }
}
