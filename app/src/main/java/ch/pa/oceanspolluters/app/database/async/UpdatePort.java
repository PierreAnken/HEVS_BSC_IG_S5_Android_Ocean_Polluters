package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class UpdatePort extends AsyncTask<PortEntity, Void, Void> {

    private static final String TAG = "UpdatePort";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public UpdatePort(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(PortEntity... params) {
        try {
            for (PortEntity port : params)
                ((BaseApp) mApplication).getPortRepository()
                        .update(port);
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