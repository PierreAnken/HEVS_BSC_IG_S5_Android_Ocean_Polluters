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
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.database.repository.ItemRepository;


public class ItemListViewModel extends AndroidViewModel {

    private static final String TAG = "ItemListViewModel";

    private ItemRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ItemWithType>> mObservableItems;

    private ItemListViewModel(@NonNull Application application,
                              final int containerId, ItemRepository ItemRepository) {
        super(application);

        mRepository = ItemRepository;

        mObservableItems = new MediatorLiveData<>();
        
        // set by default null, until we get data from the database.
        mObservableItems.setValue(null);

        LiveData<List<ItemWithType>> ItemsFromContainer = ItemRepository.getItemsFromContainerLD(containerId);
                
        // observe the changes of the entities from the database and forward them
        mObservableItems.addSource(ItemsFromContainer, mObservableItems::setValue);
        
    }

    /**
     * A creator is used to inject the Item id into the ViewModel
     */
    public static class FactoryItems extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mContainerId;

        private final ItemRepository mItemRepository;


        public FactoryItems(@NonNull Application application, int containerId) {
            mApplication = application;
            mContainerId = containerId;
            mItemRepository = ((BaseApp) application).getItemRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ItemListViewModel(mApplication, mContainerId, mItemRepository);
        }
    }

    /**
     * Expose the LiveData ItemItems query so the UI can observe it.
     */
    public LiveData<List<ItemWithType>> getContainerItems() {
        return mObservableItems;
    }
}
