package com.njupt.Utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public String getDateByTimestamp(long Timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        Date date = new Date(Timestamp);
        return simpleDateFormat.format(date);
    }

    public String FormatData(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        return simpleDateFormat.format(date);
    }
}
