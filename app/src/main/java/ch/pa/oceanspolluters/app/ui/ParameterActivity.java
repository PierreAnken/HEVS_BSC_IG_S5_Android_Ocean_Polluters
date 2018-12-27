package ch.pa.oceanspolluters.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.util.LanguageHelper;

import static ch.pa.oceanspolluters.app.util.LanguageHelper.getUserLanguage;

public class ParameterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ParameterActivity";
    private Spinner mLanguageSpinner;
    private String currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.title_activity_parameters));

        //get app version from remote parameters
        TextView versionApp = findViewById(R.id.versionApp);
        versionApp.setText(BaseApp.getFbRemoteConfig().getString("versionApp"));

        mLanguageSpinner = findViewById(R.id.spinner_language);

        //setup language spinner
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, new String[]{getString(R.string.langFr), getString(R.string.langEn)});
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLanguageSpinner.setAdapter(languageAdapter);


        //get app language
        currentLang = getUserLanguage(this) == null ? "fr" : getUserLanguage(this);

        Log.d(TAG, "PA_Debug current language " + currentLang);

        //set selected spinner value
        if (currentLang.contains("fr")) {
            mLanguageSpinner.setSelection(0);
        } else {
            mLanguageSpinner.setSelection(1);
        }
        mLanguageSpinner.setOnItemSelectedListener(this);

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
        String languageSpinner = "en";
        if (mLanguageSpinner.getSelectedItemId() == 0)
            languageSpinner = "fr";

        Log.d(TAG, "PA_Debug language spinner " + languageSpinner);

        if (!currentLang.equals(languageSpinner)) {
            LanguageHelper.storeUserLanguage(this, languageSpinner);
            LanguageHelper.updateLanguage(this, languageSpinner);
            ((BaseApp) getApplication()).setHomeNeedRefresh(true);
            this.recreate();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
