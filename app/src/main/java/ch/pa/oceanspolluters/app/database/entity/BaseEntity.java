package ch.pa.oceanspolluters.app.database.entity;

import com.google.firebase.database.FirebaseDatabase;

public abstract class BaseEntity {
    protected static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    private String FB_Key;
    public String getFB_Key(){return FB_Key;}
    public void setFB_Key(String fb_key){this.FB_Key = fb_key;}
}
