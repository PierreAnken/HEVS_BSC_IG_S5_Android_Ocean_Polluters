package ch.pa.oceanspolluters.app.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ch.pa.oceanspolluters.app.R;

public class BaseToolbarActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Toolbar getToolbar(){
        setContentView(R.layout.toolbar);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.global_toolbar);
        return myToolbar;
    }
}
