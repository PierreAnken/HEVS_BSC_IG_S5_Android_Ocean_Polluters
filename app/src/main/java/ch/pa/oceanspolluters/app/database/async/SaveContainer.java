package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class SaveContainer extends AsyncTask<ContainerEntity, Void, Void> {

    private static final String TAG = "SaveContainer";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public SaveContainer(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(ContainerEntity... params) {
        try {
            for (ContainerEntity container : params) {

                if (container.getId() == null) {
                    ((BaseApp) mApplication).getContainerRepository()
                            .insert(container);
                    Log.d(TAG, "PA_Debug insert: " + container.getDockPosition());
                } else {
                    ((BaseApp) mApplication).getContainerRepository()
                            .update(container);
                    Log.d(TAG, "PA_Debug update: " + container.getDockPosition());
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