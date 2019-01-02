package ch.pa.oceanspolluters.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.LanguageEntity;

public class ParameterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ParameterActivity";
    private Spinner mLanguageSpinner;
    private static List<LanguageEntity> languages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_activity_parameters));

        //get app version from remote parameters
        TextView versionApp = findViewById(R.id.versionApp);
        versionApp.setText(BaseApp.getFbRemoteConfig().getString("versionApp"));

       /* mLanguageSpinner = findViewById(R.id.spinner_language);

        //setup language spinner

        FirebaseDatabase.getInstance().getReference("languages").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            languages = new ArrayList<>();
                            for(DataSnapshot lang : dataSnapshot.getChildren()){
                                languages.add(lang.getValue(LanguageEntity.class));

                            }
                            ArrayAdapter<LanguageEntity> languageAdapter = new ArrayAdapter<LanguageEntity>(getApplicationContext(), android.R.layout.simple_spinner_item, languages);
                            languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mLanguageSpinner.setAdapter(languageAdapter);

                            if (languages.get(0).isActive()) {
                                mLanguageSpinner.setSelection(0);
                            } else {
                                mLanguageSpinner.setSelection(1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
        mLanguageSpinner.setOnItemSelectedListener(this);*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //get spinner lanugage
        String languageSpinner = ((LanguageEntity)mLanguageSpinner.getSelectedItem()).getIso();

        Log.d(TAG, "PA_Debug language spinner " + languageSpinner);

        if(!languageSpinner.equals(LanguageEntity.getAppLanguage(this))){
            LanguageEntity.setCurrentLang(getBaseContext(),languageSpinner);
            //this.recreate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
