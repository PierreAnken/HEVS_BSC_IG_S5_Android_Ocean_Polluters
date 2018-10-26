package ch.pa.oceanspolluters.app.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.database.repository.ShipRepository;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;


public class ShipListViewModel extends AndroidViewModel {

    private static final String TAG = "ShipListViewModel";

    private ShipRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ShipWithContainer>> mObservableCaptainShips;

    public ShipListViewModel(@NonNull Application application,
                             final int captainId, ShipRepository shipRepository) {
        super(application);

        mRepository = shipRepository;

        mObservableCaptainShips = new MediatorLiveData<>();
        
        // set by default null, until we get data from the database.
        mObservableCaptainShips.setValue(null);

        LiveData<List<ShipWithContainer>> shipsFull = shipRepository.getShipsFromCaptain(captainId);
                
        // observe the changes of the entities from the database and forward them
        mObservableCaptainShips.addSource(shipsFull, mObservableCaptainShips::setValue);
        
    }

    /**
     * A creator is used to inject the ship id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mCaptainId;

        private final ShipRepository mShipRepository;


        public Factory(@NonNull Application application, int captainId) {
            mApplication = application;
            mCaptainId = captainId;
            mShipRepository = ((BaseApp) application).getShipRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ShipListViewModel(mApplication, mCaptainId, mShipRepository);
        }
    }

    /**
     * Expose the LiveData ShipShips query so the UI can observe it.
     */
    public LiveData<List<ShipWithContainer>> getCaptainShips() {
        return mObservableCaptainShips;
    }
}
