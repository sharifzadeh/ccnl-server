package edu.dartmouth.ccnl.ridmp.com;

import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

public class ReportUtils {

    public static String getStringValue(Object object) {
        if (object == null) {
            return "-";
        } else {
            return String.valueOf(object);
        }
    }

    public static java.sql.Date getDateToken(String token) throws QueryParsingException {
        //format in right to left inputs is reverse.
        String pattern = "yyyy/MM/dd";
        if (StringUtils.isNotEmpty(token)) {
            int last = token.lastIndexOf("/");
            int first = token.indexOf("/");

            if ((first < 4) && (last < 7)) {
                pattern = "dd/MM/yyyy";
            }
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
            return null;
    }
}
