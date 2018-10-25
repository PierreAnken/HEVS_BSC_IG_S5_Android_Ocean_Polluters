package ch.pa.oceanspolluters.app.util;

import java.util.Calendar;
import java.util.Date;

//toolbox
public class TB {

    public static String getShortDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String day = Calendar.DAY_OF_MONTH < 10 ? "0"+Calendar.DAY_OF_MONTH :""+Calendar.DAY_OF_MONTH;
        String month = Calendar.MONTH < 10 ? "0"+Calendar.MONTH :""+Calendar.MONTH;

        return day+"/"+month+"/"+cal.get(Calendar.YEAR);
    }
}
