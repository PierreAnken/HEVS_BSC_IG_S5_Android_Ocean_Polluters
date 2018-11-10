package ch.pa.oceanspolluters.app.database.entity;

import android.app.Application;
import android.arch.persistence.room.Ignore;
import android.content.Context;
import android.util.Log;

import javax.security.auth.callback.Callback;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.async.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;

public abstract class BaseEntity {
    @Ignore
    private OperationMode operationMode;

    public OperationMode getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(OperationMode operationMode) {
        this.operationMode = operationMode;
    }
}
