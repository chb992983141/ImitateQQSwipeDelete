package com.duff.ImitateQQSwipeDelete;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ezez-c on 2017/3/2.
 */
public class DateUtilx {

        public Timestamp DateTime2Timestamp() {
            Date dateTime = new Date();
            Timestamp timestamp = new Timestamp(dateTime.getTime());
            System.out.println("存进去时间是--->"+timestamp);
            return timestamp;

        }

        public long Timestamp2DateTime(String strDate) throws ParseException {
            String str;
            Date date;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(strDate);
            System.out.println("取出来时间是--->"+date.toString());
            return date.getTime();
        }
}
