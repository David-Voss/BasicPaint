package toolbox;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class TimeStamp {

    public static String dateTime() {
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(systemLocale);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateTimeString = dateTimeNow.format(dateFormatter) + " " + dateTimeNow.format(timeFormatter);
        return dateTimeString;
    }

    public static String date() {
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String dateString = dateTimeNow.format(dateFormatter);
        return dateString;
    }

    public static String time() {
        Locale systemLocale = Locale.getDefault();
        ZoneId systemTimeZone = ZoneId.systemDefault();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG).withLocale(systemLocale);

        ZonedDateTime dateTimeNow = ZonedDateTime.now(systemTimeZone);
        String timeString = dateTimeNow.format(timeFormatter);
        return timeString;
    }
}
