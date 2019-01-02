package ch.pa.oceanspolluters.app.util;

import android.util.Log;

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


