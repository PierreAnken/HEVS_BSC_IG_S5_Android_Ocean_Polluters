package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class CaptainShipAddEditActivity extends AppCompatActivity {

    private ShipWithContainer mShip;
    private ShipViewModel mViewModel;

    private static final String TAG = "CaptainShipAddEdit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_ship_add_edit);


        Intent shipDetail = getIntent();
        int shipId = shipDetail.getStringExtra("shipId") != null ? Integer.parseInt(shipDetail.getStringExtra("shipId")): 0;

        if(shipId > 0)
            setTitle(getString(R.string.ship_edit));
        else
            setTitle(getString(R.string.ship_add));

        Log.d(TAG, "PA_Debug received ship id from intent:" + shipId);

        //get ship and display it in form
        ShipViewModel.FactoryShip factory = new ShipViewModel.FactoryShip(getApplication(), shipId);
        mViewModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
        mViewModel.getShip().observe(this, ship -> {
            if (ship != null) {
                mShip = ship;
                Log.d(TAG, "PA_Debug ship id from factory:" + ship.ship.getId());
                updateView();
            }
        });
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");
        if(mShip != null){
            ((EditText)findViewById(R.id.ae_ship_name)).setText(mShip.ship.getName());
            //((EditText)findViewById(R.id.ae_destination_port)).setText(mShip.port.getName());
            ((EditText)findViewById(R.id.ae_departure_date)).setText(TB.getShortDate(mShip.ship.getDepartureDate()));

        }
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
