package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.repository.ContainerRepository;


public class ContainerViewModel extends AndroidViewModel {

    private static final String TAG = "ContainerViewModel";

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ContainerWithItem> mObservableContainer;

    public ContainerViewModel(@NonNull Application application,
                              final int containerId, ContainerRepository containerRepository) {
        super(application);

        ContainerRepository mRepository = containerRepository;

        mObservableContainer = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableContainer.setValue(null);

        LiveData<ContainerWithItem> container = mRepository.getContainerLD(containerId);

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

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
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

}
