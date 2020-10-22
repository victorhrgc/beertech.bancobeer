package beertech.becks.api.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.ZoneId.systemDefault;
import static java.time.format.DateTimeFormatter.ofPattern;

public class DateUtil {

    public static String zonedDateTimeInString(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(ofPattern("dd/MM/yyyy"));
    }

    public static ZonedDateTime stringInZonedDateTime(String dateString) {
        LocalDate localDate = LocalDate.parse(dateString, ofPattern("dd/MM/yyyy"));
        return localDate.atStartOfDay(systemDefault());
    }

    public static LocalDateTime stringInLocalDateTime(String dataString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
        return LocalDateTime.parse(dataString, formatter);
    }

}
