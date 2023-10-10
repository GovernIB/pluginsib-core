package org.fundaciobit.pluginsib.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author anadal
 *
 */
public class ISO8601 {

    /** Transform Date to ISO 8601 string. */
    public static String dateToISO8601(Date date) {
        String formatted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }

    /** Transform ISO 8601 string to Date. */
    public static Date ISO8601ToDate(final String iso8601string) throws ParseException {
        String s;
        try {
            s = iso8601string.replace("Z", "+00:00");
            s = s.substring(0, 22) + s.substring(23); // to get rid of the ":"

        } catch (Throwable e) {
            throw new ParseException("Invalid length of de the iso8601 date: ]" + iso8601string + "[", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        return date;

    }
}
