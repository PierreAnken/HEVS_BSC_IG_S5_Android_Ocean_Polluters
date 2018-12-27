package ch.pa.oceanspolluters.app.util;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import ch.pa.oceanspolluters.app.BaseApp;

public class RemoteParameters {
    private static String TAG = "RemoteParameters";

    public static void getRemoteParameters(){
        //cache from 12 houres to avoid being throttled
        BaseApp.getFbRemoteConfig().fetch(60*60*12)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        BaseApp.getFbRemoteConfig().activateFetched();
                        String version = BaseApp.getFbRemoteConfig().getString("versionApp");
                        Log.d(TAG, "PA_Debug current versionApp " + version);
                    } else {
                        Log.d(TAG, "PA_Debug error loading params: "+task.getException());
                    }
                });

    }
}


