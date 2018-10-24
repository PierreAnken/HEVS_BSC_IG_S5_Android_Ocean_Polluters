package ch.pa.oceanspolluters.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.Toast;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class BaseApp extends Application {

    private static UserEntity currentUser;

    public LayoutInflater getLayoutInflater(){
        return (LayoutInflater)getApplicationContext().getSystemService
                (getApplicationContext().LAYOUT_INFLATER_SERVICE);
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
