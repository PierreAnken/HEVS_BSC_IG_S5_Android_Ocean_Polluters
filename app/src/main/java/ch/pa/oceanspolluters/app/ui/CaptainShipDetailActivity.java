package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class CaptainShipDetailActivity extends AppCompatActivity {

    private ShipWithContainer mShip;
    private ShipViewModel mViewModel;

    private static final String TAG = "CaptainShipDetailAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_travel_detail);

        Intent shipDetail = getIntent();
        int shipId = Integer.parseInt(shipDetail.getStringExtra("shipId"));
        Log.d(TAG, "PA_Debug received ship id from intent:" + shipId);

        //get ship and display it
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
            ((TextView)findViewById(R.id.t_ship_name)).setText(mShip.ship.getName());
            ((TextView)findViewById(R.id.t_destination_port)).setText(mShip.port.getName());
            ((TextView)findViewById(R.id.t_departure_date)).setText(TB.getShortDate(mShip.ship.getDepartureDate()));

            int containerLoaded = 0;
            int totalLoadedWeight = 0;
            int shipContainers = 0;

            if(mShip.containers != null)
                shipContainers = mShip.containers.size();

            for (ContainerWithItem container: mShip.containers
                 ) {
                if(container.container.getLoaded()){
                    containerLoaded++;
                    totalLoadedWeight+= container.getWeight();
                }
            }
            ((TextView)findViewById(R.id.t_container_loaded)).setText(containerLoaded+"/"+shipContainers);
            ((TextView)findViewById(R.id.t_total_weight)).setText(totalLoadedWeight+"/"+mShip.ship.getMaxLoadKg());

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
