package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.repository.ContainerRepository;


public class ContainerListViewModel extends AndroidViewModel {

    private static final String TAG = "ContainerListViewModel";

    private ContainerRepository mRepository;

    private static boolean onlyToLoad;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ContainerWithItem>> mObservableContainers;

    public ContainerListViewModel(@NonNull Application application,
                                   final int shipId, ContainerRepository containerRepository, boolean onlyToLoad) {
        super(application);

        mRepository = containerRepository;

        mObservableContainers = new MediatorLiveData<>();

        this.onlyToLoad = onlyToLoad;
        
        // set by default null, until we get data from the database.
        mObservableContainers.setValue(null);

        LiveData<List<ContainerWithItem>> ContainersFull;

        if (shipId < 0) {
            if (onlyToLoad) {
                ContainersFull = containerRepository.getContainerToLoadLD();
            } else {
                ContainersFull = containerRepository.getContainersLD();
            }
        } else {
            ContainersFull = containerRepository.getByShipIdLD(shipId);
        }

        // observe the changes of the entities from the database and forward them
        mObservableContainers.addSource(ContainersFull, mObservableContainers::setValue);
        
    }

    /**
     * A creator is used to inject the sontainer id into the ViewModel
     */
    public static class FactoryContainers extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mShipId;

        private final ContainerRepository mContainerRepository;


        public FactoryContainers(@NonNull Application application, int shipId, boolean onlyToLoadS) {
            mApplication = application;
            mShipId = shipId;
            mContainerRepository = ((BaseApp) application).getContainerRepository();
            onlyToLoad = onlyToLoadS;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ContainerListViewModel(mApplication, mShipId, mContainerRepository, onlyToLoad);
        }
    }

    /**
     * Expose the LiveData ContainerContainers query so the UI can observe it.
     */
    public LiveData<List<ContainerWithItem>> getContainers() {
        return mObservableContainers;
    }
}
