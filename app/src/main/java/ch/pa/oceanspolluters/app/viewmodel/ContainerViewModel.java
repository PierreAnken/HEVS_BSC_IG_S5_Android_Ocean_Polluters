package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.async.UpdateContainer;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.repository.ContainerRepository;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;


public class ContainerViewModel extends AndroidViewModel {

    private static final String TAG = "ContainerViewModel";

    private ContainerRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ContainerWithItem> mObservableContainer;

    public ContainerViewModel(@NonNull Application application,
                              final int containerId, ContainerRepository containerRepository) {
        super(application);

        mRepository = containerRepository;

        mObservableContainer = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableContainer.setValue(null);

        LiveData<ContainerWithItem> container = mRepository.getContainer(containerId);

        // observe the changes of the container entity from the database and forward them
        mObservableContainer.addSource(container, mObservableContainer::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class FactoryContainer extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mContainerId;

        private final ContainerRepository mRepository;

        public FactoryContainer(@NonNull Application application, int containerId) {
            mApplication = application;
            mContainerId = containerId;
            mRepository = ((BaseApp) application).getContainerRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ContainerViewModel(mApplication, mContainerId, mRepository);
        }
    }

    /**
     * Expose the LiveData ContainerEntity query so the UI can observe it.
     */
    public LiveData<ContainerWithItem> getContainer() {
        return mObservableContainer;
    }

    public void updateContainer(ContainerEntity container, OnAsyncEventListener callback) {
        new UpdateContainer(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
                Log.d(TAG, "updateContainer: success");
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
                Log.d(TAG, "updateContainer: failure", e);
            }
        });
    }
}
