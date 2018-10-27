package ch.pa.oceanspolluters.app.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;

//toolbox
public class TB {

    public static String getShortDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }
}
