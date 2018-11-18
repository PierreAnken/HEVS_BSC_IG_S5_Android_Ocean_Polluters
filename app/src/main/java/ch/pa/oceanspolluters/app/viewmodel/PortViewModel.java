package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.repository.PortRepository;


public class PortViewModel extends AndroidViewModel {

    private static final String TAG = "PortViewModel";

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<PortEntity> mObservablePort;

    public PortViewModel(@NonNull Application application,
                         final int portId, PortRepository portRepository) {
        super(application);

        PortRepository mRepository = portRepository;

        mObservablePort = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservablePort.setValue(null);

        LiveData<PortEntity> port = mRepository.getPortLD(portId);

        // observe the changes of the port entity from the database and forward them
        mObservablePort.addSource(port, mObservablePort::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class FactoryPort extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mPortId;

        private final PortRepository mRepository;

        public FactoryPort(@NonNull Application application, int portId) {
            mApplication = application;
            mPortId = portId;
            mRepository = ((BaseApp) application).getPortRepository();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PortViewModel(mApplication, mPortId, mRepository);
        }
    }

    /**
     * Expose the LiveData PortEntity query so the UI can observe it.
     */
    public LiveData<PortEntity> getPort() {
        return mObservablePort;
    }

}
