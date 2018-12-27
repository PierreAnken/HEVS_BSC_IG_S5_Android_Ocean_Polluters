package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.Ignore;

import com.google.firebase.database.FirebaseDatabase;

import ch.pa.oceanspolluters.app.util.OperationMode;

public abstract class BaseEntity {
    protected static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    private String FB_Key;
    public String getFB_Key(){return FB_Key;}
    public void setFB_Key(String fb_key){this.FB_Key = fb_key;}
}
