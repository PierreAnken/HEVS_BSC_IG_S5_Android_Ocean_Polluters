package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class CaptainShipDetailActivity extends AppCompatActivity {

    private ShipWithContainer mShip;
    private ShipViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_travel_detail);

        Intent shipDetail = getIntent();
        int shipId = Integer.parseInt(shipDetail.getStringExtra("shipId"));


        //get ship and display it
        ShipViewModel.Factory factory = new ShipViewModel.Factory(getApplication(), shipId);
        mViewModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
        mViewModel.getShip().observe(this, ship -> {
            if (ship != null) {
                mShip = ship;
                updateView();
            }
        });
    }

    private void updateView(){
        if(mShip != null){
            ((TextView)findViewById(R.id.t_ship_name)).setText(mShip.ship.getName());
            ((TextView)findViewById(R.id.t_destination_port)).setText(mShip.port.getName());
            ((TextView)findViewById(R.id.t_departure_date)).setText(TB.getShortDate(mShip.ship.getDepartureDate()));
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
