package ch.pa.oceanspolluters.app.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import ch.pa.oceanspolluters.app.R;

//toolbox
public class TB {

    public static String getShortDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return simpleDateFormat.format(date);
    }

    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("dd-MM-yyyy");
    }

    public static void ConfirmAction(AppCompatActivity activity, String actionDescription, OnUserValidatePopupAction callback) {

        DialogInterface.OnClickListener dialogDelete = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    callback.onValidate();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(actionDescription).setPositiveButton(activity.getString(R.string.yes), dialogDelete)
                .setNegativeButton(activity.getString(R.string.no), dialogDelete).show();

    }

}
