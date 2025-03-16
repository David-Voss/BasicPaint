package toolbox;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Utility class for retrieving formatted date and time strings.
 */
public class DateTimeStamp {

    private static final Locale systemLocale = Locale.getDefault();
    private static final ZoneId systemTimeZone = ZoneId.systemDefault();

    /**
     * Returns the current date and time formatted in a medium date style and short time style.
     * <p>
     * The format is determined based on the system locale.
     * Example output: "12 Mar 2024 14:30"
     * </p>
     *
     * @return A formatted string representing the current date and time.
     */
    public static String dateTime() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(systemLocale);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateTimeString = dateTimeNow.format(dateFormatter) + " " + dateTimeNow.format(timeFormatter);
        return dateTimeString;
    }

    /**
     * Returns the current date formatted in a full date style.
     * <p>
     * The format is determined based on the system locale.
     * Example output: "Tuesday, 12 March 2024"
     * </p>
     *
     * @return A formatted string representing the current date.
     */
    public static String date() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateString = dateTimeNow.format(dateFormatter);
        return dateString;
    }

    /**
     * Returns the current time formatted in a long time style.
     * <p>
     * The format is determined based on the system locale.
     * Example output: "14:30:15 CET"
     * </p>
     *
     * @return A formatted string representing the current time.
     */
    public static String time() {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String timeString = dateTimeNow.format(timeFormatter);
        return timeString;
    }
}
