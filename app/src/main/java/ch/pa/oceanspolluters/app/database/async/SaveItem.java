package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class SaveItem extends AsyncTask<ItemEntity, Void, Void> {

    private static final String TAG = "SaveItem";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public SaveItem(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(ItemEntity... params) {
        try {
            for (ItemEntity item : params) {
                if (item.getId() == null) {
                    ((BaseApp) mApplication).getItemRepository()
                            .insert(item);
                    Log.d(TAG, "PA_Debug insert: " + item.getItemTypeId());
                } else {
                    ((BaseApp) mApplication).getItemRepository()
                            .update(item);
                    Log.d(TAG, "PA_Debug update: " + item.getItemTypeId());
                }
            }
        } catch (Exception e) {
            mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (mCallBack != null) {
            if (mException == null) {
                mCallBack.onSuccess();
            } else {
                mCallBack.onFailure(mException);
            }
        }
    }
}