package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ch.pa.oceanspolluters.app.R;

public class CaptainShipDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_travel_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent shipDetail = getIntent();
        String shipName = shipDetail.getStringExtra("shipName");
        ((TextView)findViewById(R.id.shipName)).setText(shipName);

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
}
