package ch.pa.oceanspolluters.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class BaseApp extends Application {

    private static UserEntity currentUser;

    @Override
    public void onCreate() {
        super.onCreate();

        /*init database at startup
        / otherwise Login Spinner is empty*/
        AppDatabase.getInstance(this);

    }


    public void connectUser(UserEntity user){
        currentUser = user;
        displayShortToast(currentUser.getName() + " connected");
    }

    public void disconnectUser(){

      if(currentUser != null)
      {
          displayShortToast(currentUser.getName() + " disconnected");
          currentUser = null;
      }
    }

    public UserEntity getCurrentUser(){
        return currentUser;
    }


    public void displayShortToast(String text){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
