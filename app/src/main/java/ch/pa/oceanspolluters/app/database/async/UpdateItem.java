package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class UpdateItem extends AsyncTask<ItemEntity, Void, Void> {

    private static final String TAG = "UpdateItem";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public UpdateItem(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(ItemEntity... params) {
        try {
            for (ItemEntity item : params)
                ((BaseApp) mApplication).getItemRepository()
                        .update(item);
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