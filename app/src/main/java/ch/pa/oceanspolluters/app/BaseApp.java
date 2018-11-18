package ch.pa.oceanspolluters.app;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.Toast;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.repository.ContainerRepository;
import ch.pa.oceanspolluters.app.database.repository.ItemRepository;
import ch.pa.oceanspolluters.app.database.repository.ItemTypeRepository;
import ch.pa.oceanspolluters.app.database.repository.PortRepository;
import ch.pa.oceanspolluters.app.database.repository.ShipRepository;
import ch.pa.oceanspolluters.app.database.repository.UserRepository;
import ch.pa.oceanspolluters.app.util.LanguageHelper;

public class BaseApp extends Application {

    private static UserEntity currentUser;

    public LayoutInflater getLayoutInflater(){
        return (LayoutInflater)getApplicationContext().getSystemService
                (getApplicationContext().LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppDatabase.getInstance(getApplicationContext());
        initLanguage();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        initLanguage();
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

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public ShipRepository getShipRepository() {
        return ShipRepository.getInstance(getDatabase());
    }

    public ContainerRepository getContainerRepository() {
        return ContainerRepository.getInstance(getDatabase());
    }

    public PortRepository getPortRepository() {
        return PortRepository.getInstance(getDatabase());
    }

    public UserRepository getUserRepository() {
        return UserRepository.getInstance(getDatabase());
    }

    public ItemRepository getItemRepository() {
        return ItemRepository.getInstance(getDatabase());
    }

    public ItemTypeRepository getItemTypeRepository() {
        return ItemTypeRepository.getInstance(getDatabase());
    }


    public void displayShortToast(String text){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initLanguage() {
        String ul = LanguageHelper.getUserLanguage(this);
        // if null the language doesn't need to be changed as the user has not chosen one.
        if (ul != null) {
            LanguageHelper.updateLanguage(this, ul);
        }
    }
}
