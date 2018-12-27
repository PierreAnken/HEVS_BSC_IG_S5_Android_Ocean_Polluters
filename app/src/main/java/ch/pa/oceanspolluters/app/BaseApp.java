package ch.pa.oceanspolluters.app;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import ch.pa.oceanspolluters.app.database.DataGenerator;
import ch.pa.oceanspolluters.app.database.entity.LanguageEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.RemoteParameters;

public class BaseApp extends Application {

    private static UserEntity currentUser;
    private static boolean HomeNeedRefresh = false;
    private static FirebaseRemoteConfig mFirebaseRemoteConfig;

    //user after language change
    public static boolean NeedHomeRefresh() {
        return HomeNeedRefresh;
    }
    public static FirebaseRemoteConfig getFbRemoteConfig(){
        return mFirebaseRemoteConfig;
    }

    public static void setHomeNeedRefresh(boolean homeNeedRefresh) {
        HomeNeedRefresh = homeNeedRefresh;
    }


    public LayoutInflater getLayoutInflater(){
        return (LayoutInflater)getApplicationContext().getSystemService
                (getApplicationContext().LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataGenerator.initFireBaseData();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        RemoteParameters.getRemoteParameters();
        LanguageEntity.initAppLanguage(getBaseContext());
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
