package ch.pa.oceanspolluters.app.database.async;

import android.app.Application;
import android.os.AsyncTask;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

public class UpdateUser extends AsyncTask<UserEntity, Void, Void> {

    private static final String TAG = "UpdateUser";

    private Application mApplication;
    private OnAsyncEventListener mCallBack;
    private Exception mException;

    public UpdateUser(Application application, OnAsyncEventListener callback) {
        mApplication = application;
        mCallBack = callback;
    }

    @Override
    protected Void doInBackground(UserEntity... params) {
        try {
            for (UserEntity user : params)
                ((BaseApp) mApplication).getUserRepository()
                        .update(user);
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