package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.Ignore;

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

    @Ignore
    private String FB_Key;
    public String getFB_Key(){return FB_Key;}
    public void setFB_Key(String fb_key){this.FB_Key = fb_key;}
}
