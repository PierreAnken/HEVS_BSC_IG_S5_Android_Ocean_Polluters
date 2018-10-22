package ch.pa.oceanspolluters.app;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class BaseApp extends Application {

    private static UserEntity currentUser;

    public static void setCurrentUser(UserEntity user){
        currentUser = user;
    }

    public static UserEntity getCurrentUser(){
        return currentUser;
    }


    public void displayShortToast(String text){
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
