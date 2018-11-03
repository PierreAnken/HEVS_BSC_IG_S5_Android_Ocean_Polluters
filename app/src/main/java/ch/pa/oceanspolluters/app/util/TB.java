package ch.pa.oceanspolluters.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;

//toolbox
public class TB {

    public static String getShortDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }
}
