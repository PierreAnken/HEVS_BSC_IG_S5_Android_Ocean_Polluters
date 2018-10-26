package ch.pa.oceanspolluters.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.widget.Toast;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.repository.ContainerRepository;
import ch.pa.oceanspolluters.app.database.repository.ItemRepository;
import ch.pa.oceanspolluters.app.database.repository.PortRepository;
import ch.pa.oceanspolluters.app.database.repository.ShipRepository;
import ch.pa.oceanspolluters.app.database.repository.UserRepository;

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

    public void displayShortToast(String text){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
