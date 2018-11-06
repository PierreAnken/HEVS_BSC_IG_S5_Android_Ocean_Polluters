package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class SaveUser extends AsyncTask<UserEntity, Void, Void> {

    private static final String TAG = "SaveUser";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public SaveUser(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(UserEntity... params) {
        try {
            for (UserEntity user : params) {
                if (user.getId() == null) {
                    ((BaseApp) mApplication).getUserRepository()
                            .insert(user);
                    Log.d(TAG, "PA_Debug insert: " + user.getName());
                } else {
                    ((BaseApp) mApplication).getUserRepository()
                            .update(user);
                    Log.d(TAG, "PA_Debug update: " + user.getName());
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