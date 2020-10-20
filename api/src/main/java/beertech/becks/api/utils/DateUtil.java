package beertech.becks.api.utils;

import java.time.LocalDate;
import java.time.ZonedDateTime;

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

}
