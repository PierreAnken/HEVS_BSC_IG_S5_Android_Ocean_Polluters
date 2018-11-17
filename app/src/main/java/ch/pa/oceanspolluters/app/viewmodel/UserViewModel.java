package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.repository.UserRepository;


public class UserViewModel extends AndroidViewModel {

    private static final String TAG = "UserViewModel";

    private UserRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<UserEntity> mObservableUser;

    public UserViewModel(@NonNull Application application,
                         final int userId, UserRepository userRepository) {
        super(application);

        mRepository = userRepository;

        mObservableUser = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableUser.setValue(null);

        LiveData<UserEntity> user = mRepository.getUserLD(userId);

        // observe the changes of the user entity from the database and forward them
        mObservableUser.addSource(user, mObservableUser::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class FactoryUser extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mUserId;

        private final UserRepository mRepository;

        public FactoryUser(@NonNull Application application, int userId) {
            mApplication = application;
            mUserId = userId;
            mRepository = ((BaseApp) application).getUserRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new UserViewModel(mApplication, mUserId, mRepository);
        }
    }

    /**
     * Expose the LiveData UserEntity query so the UI can observe it.
     */
    public LiveData<UserEntity> getUser() {
        return mObservableUser;
    }

}
