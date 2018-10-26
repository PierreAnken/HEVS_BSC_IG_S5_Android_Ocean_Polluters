package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class UpdateShip extends AsyncTask<ShipEntity, Void, Void> {

    private static final String TAG = "UpdateShip";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public UpdateShip(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(ShipEntity... params) {
        try {
            for (ShipEntity ship : params)
                ((BaseApp) mApplication).getShipRepository()
                        .update(ship);
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