package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.repository.UserRepository;


public class UserListViewModel extends AndroidViewModel {

    private static final String TAG = "UserListViewModel";

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<UserEntity>> mObservableUsers;

    private UserListViewModel(@NonNull Application application, UserRepository UserRepository) {
        super(application);

        ch.pa.oceanspolluters.app.database.repository.UserRepository mRepository = UserRepository;

        mObservableUsers = new MediatorLiveData<>();
        
        // set by default null, until we get data from the database.
        mObservableUsers.setValue(null);

        LiveData<List<UserEntity>> Users = UserRepository.getUsersLD();
                
        // observe the changes of the entities from the database and forward them
        mObservableUsers.addSource(Users, mObservableUsers::setValue);
        
    }

    /**
     * A creator is used to inject the User id into the ViewModel
     */
    public static class FactoryUsers extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final UserRepository mUserRepository;


        public FactoryUsers(@NonNull Application application) {
            mApplication = application;
            mUserRepository = ((BaseApp) application).getUserRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserListViewModel(mApplication, mUserRepository);
        }
    }

    /**
     * Expose the LiveData UserUsers query so the UI can observe it.
     */
    public LiveData<List<UserEntity>> getUsers() {
        return mObservableUsers;
    }
}
