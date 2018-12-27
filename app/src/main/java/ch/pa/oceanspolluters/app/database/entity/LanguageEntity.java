package ch.pa.oceanspolluters.app.database.entity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;

import static android.support.constraint.Constraints.TAG;

public class LanguageEntity extends BaseEntity {

    private static final String TAG = "LanguageEntity";

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private String iso;
    private boolean active;

    public LanguageEntity(){}

    public LanguageEntity(String iso, boolean active){
        this.iso = iso;
        this.active = active;
    }

    public String toString(){
        return getIso();
    }

    @Exclude
    public static void setCurrentLang(Context context, String iso){
        getLangList(new OnAsyncEventListener() {
            @Override
            public void onSuccess(ArrayList resultData) {
                ArrayList<LanguageEntity> languages = ((ArrayList<LanguageEntity>) resultData);
                for(LanguageEntity lang : languages){
                    if(lang.iso.equals(iso)){
                        fireBaseDB.getReference("languages/"+lang.getFB_Key()+"/active").setValue(true);
                        setAppLanguage(context, iso);
                        BaseApp.setHomeNeedRefresh(true);
                    }else{
                        fireBaseDB.getReference("languages/"+lang.getFB_Key()+"/active").setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    @Exclude
    public static void initAppLanguage(Context context){
        getLangList(new OnAsyncEventListener() {
            @Override
            public void onSuccess(ArrayList resultData) {

                LanguageEntity FBLang = null;
                for(Object lang : resultData){
                    if(((LanguageEntity)lang).active){
                        FBLang = ((LanguageEntity)lang);
                        break;
                    };
                }

                if(FBLang != null){
                    if(!getAppLanguage(context).equals(FBLang.iso)){
                        setAppLanguage(context,FBLang.iso);
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }
    @Exclude
    public static String getAppLanguage(Context context){
        Configuration config = context.getResources().getConfiguration();
        Locale locale = config.getLocales().get(0);
        Log.d(TAG, "PA_Debug current app locale language:" + locale.getLanguage());

        return locale.getLanguage();
    }

    @Exclude
    public static void setAppLanguage(Context context, String iso){

        Log.d(TAG, "PA_Debug setting app locale language to:" + iso);

        Locale locale = new Locale(iso);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config= new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);

        Log.d(TAG, "PA_Debug test app local language :" + LanguageEntity.getAppLanguage(context));
    }

    @Exclude
    private static void getLangList(OnAsyncEventListener listener){

        fireBaseDB.getReference("languages").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ArrayList<LanguageEntity> languages = new ArrayList<>();
                    for(DataSnapshot lang : dataSnapshot.getChildren()){
                        languages.add(lang.getValue(LanguageEntity.class));
                    }
                    listener.onSuccess(languages);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError.toException());

            }
        });
    }
}
