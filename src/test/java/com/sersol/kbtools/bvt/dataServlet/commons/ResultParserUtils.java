package com.sersol.kbtools.bvt.dataServlet.commons;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ResultParserUtils {

    /**
     * Converts the string to a DateTime object, so that it's easier to manipulate later.
     * The date string comes in this format "yyyy-MM-dd HH:mm:ss.SSS".
     * @param date date string formatted like this: yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public DateTime formatDateString(String date){
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return formatter.parseDateTime(date);
    }
}
