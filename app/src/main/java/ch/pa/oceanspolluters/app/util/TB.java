package ch.pa.oceanspolluters.app.util;

import java.util.Calendar;
import java.util.Date;

//toolbox
public class TB {

    public static String getShortDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);

        String dayS = day < 10 ? "0"+day :""+day;
        String monthS = month < 10 ? "0"+month :""+month;

        return dayS+"/"+monthS+"/"+cal.get(Calendar.YEAR);
    }
}
