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
import ch.pa.oceanspolluters.app.database.async.SaveItem;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.repository.ItemRepository;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;


public class ItemViewModel extends AndroidViewModel {

    private static final String TAG = "ItemViewModel";

    private ItemRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<ItemEntity> mObservableItem;

    public ItemViewModel(@NonNull Application application,
                         final int containerId, ItemRepository containerRepository) {
        super(application);

        mRepository = containerRepository;

        mObservableItem = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableItem.setValue(null);

        LiveData<ItemEntity> container = mRepository.getItemLD(containerId);

        // observe the changes of the container entity from the database and forward them
        mObservableItem.addSource(container, mObservableItem::setValue);
    }

    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class FactoryItem extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mItemId;

        private final ItemRepository mRepository;

        public FactoryItem(@NonNull Application application, int containerId) {
            mApplication = application;
            mItemId = containerId;
            mRepository = ((BaseApp) application).getItemRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ItemViewModel(mApplication, mItemId, mRepository);
        }
    }

    /**
     * Expose the LiveData ItemEntity query so the UI can observe it.
     */
    public LiveData<ItemEntity> getItem() {
        return mObservableItem;
    }

    public void updateItem(ItemEntity item, OnAsyncEventListener callback) {
        new SaveItem(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
                Log.d(TAG, "updateItem: success");
            }

            @Override
            public void onFailure(Exception e) {
                callback.onFailure(e);
                Log.d(TAG, "updateItem: failure", e);
            }
        });
    }
}
