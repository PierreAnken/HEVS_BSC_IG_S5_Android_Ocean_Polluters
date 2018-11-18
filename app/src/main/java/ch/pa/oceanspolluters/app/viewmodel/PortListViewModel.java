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
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.repository.PortRepository;


public class PortListViewModel extends AndroidViewModel {

    private static final String TAG = "PortListViewModel";

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<PortEntity>> mObservablePorts;

    private PortListViewModel(@NonNull Application application, PortRepository PortRepository) {
        super(application);

        ch.pa.oceanspolluters.app.database.repository.PortRepository mRepository = PortRepository;

        mObservablePorts = new MediatorLiveData<>();
        
        // set by default null, until we get data from the database.
        mObservablePorts.setValue(null);

        LiveData<List<PortEntity>> Ports = PortRepository.getPortsLD();
                
        // observe the changes of the entities from the database and forward them
        mObservablePorts.addSource(Ports, mObservablePorts::setValue);
        
    }

    /**
     * A creator is used to inject the Port id into the ViewModel
     */
    public static class FactoryPorts extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final PortRepository mPortRepository;


        public FactoryPorts(@NonNull Application application) {
            mApplication = application;
            mPortRepository = ((BaseApp) application).getPortRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PortListViewModel(mApplication, mPortRepository);
        }
    }

    /**
     * Expose the LiveData PortPorts query so the UI can observe it.
     */
    public LiveData<List<PortEntity>> getPorts() {
        return mObservablePorts;
    }
}
