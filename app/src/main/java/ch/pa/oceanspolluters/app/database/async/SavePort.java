package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class SavePort extends AsyncTask<PortEntity, Void, Void> {

    private static final String TAG = "SavePort";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public SavePort(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(PortEntity... params) {
        try {
            for (PortEntity port : params) {
                if (port.getId() == null) {
                    ((BaseApp) mApplication).getPortRepository()
                            .insert(port);
                    Log.d(TAG, "PA_Debug insert: " + port.getName());
                } else {
                    ((BaseApp) mApplication).getPortRepository()
                            .update(port);
                    Log.d(TAG, "PA_Debug update: " + port.getName());
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