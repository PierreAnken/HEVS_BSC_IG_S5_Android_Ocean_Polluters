package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.PortListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class CaptainShipAddEditActivity extends AppCompatActivity {

    private ShipWithContainer mShip;
    private ShipViewModel mShipModel;
    private PortListViewModel mPortsModel;
    private ArrayAdapter<String> portsAdapter;
    private List<PortEntity> mPorts;

    private EditText shipName;
    private EditText maxWeight;
    private EditText departureDate;
    private Spinner portsSpinner;

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

        shipName = findViewById(R.id.ae_ship_name);
        departureDate = findViewById(R.id.ae_departure_date);
        portsSpinner = findViewById(R.id.ae_destination_port_spinner);
        maxWeight = findViewById(R.id.ae_max_weight);

        Log.d(TAG, "PA_Debug received ship id from intent:" + shipId);

        //get ship and display it in form
        ShipViewModel.FactoryShip factory = new ShipViewModel.FactoryShip(getApplication(), shipId);
        mShipModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
        mShipModel.getShip().observe(this, ship -> {
            if (ship != null) {
                mShip = ship;
                Log.d(TAG, "PA_Debug ship id from factory:" + ship.ship.getId());
                updateView();
            }
        });

        //get port list
        PortListViewModel.FactoryPorts factoryPorts = new PortListViewModel.FactoryPorts(getApplication());
        mPortsModel = ViewModelProviders.of(this, factoryPorts).get(PortListViewModel.class);
        mPortsModel.getPorts().observe(this, ports -> {
            if (ports != null) {
                mPorts = ports;
                updateView();
            }
        });

    }

    private void saveShip(){
        boolean valid = true;

        shipName.setError(null);
        departureDate.setError(null);
        maxWeight.setError(null);

        Date convertedDate = new Date();

        String shipNameS = shipName.getText().toString();
        String departureDateS = departureDate.getText().toString();

        float maxWeightF = Float.parseFloat(maxWeight.getText().toString());

        if (maxWeightF < 10000) {
            valid = false;
            maxWeight.setError(getString(R.string.error_invalid_date_format));
        }

        if(TextUtils.isEmpty(shipNameS)){
            valid = false;
            shipName.setError(getString(R.string.error_empty_ship_name));
        }

        if(TextUtils.isEmpty(departureDateS)){
            valid = false;
            departureDate.setError(getString(R.string.error_empty_departure_date));
        }
        else{

            if(convertedDate.after(new Date() )){
                valid = false;
                departureDate.setError(getString(R.string.error_invalid_date_sup_today));
            }

            try {
                convertedDate = TB.getDateFormat().parse(departureDateS);

            } catch (java.text.ParseException e) {
                valid = false;
                departureDate.setError(getString(R.string.error_invalid_date_format) + " : " + TB.getDateFormat().toString());
            }
        }

        if(valid){
            Log.d(TAG, "PA_Debug port selected: " + portsSpinner.getSelectedItem());
            int portId = 0;
            for (PortEntity port : mPorts) {
                if (port.getName().equals(portsSpinner.getSelectedItem())) {
                    portId = port.getId();
                    break;
                }
            }


            ShipEntity ship = new ShipEntity(shipNameS, maxWeightF, ((BaseApp) getApplication()).getCurrentUser().getId(), portId, convertedDate);

            if (mShip != null) {
                ship.setId(mShip.ship.getId());
            }
            ship.setOperationMode(OperationMode.Save);

            new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
                @Override
                public void onSuccess(List result) {
                    Log.d(TAG, "PA_Debug updateShip: success");
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "PA_Debug updateShip: failure", e);
                    finish();
                }
            }).execute(ship);
        }
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");
        if(mShip != null){
            shipName.setText(mShip.ship.getName());
            departureDate.setText(TB.getShortDate(mShip.ship.getDepartureDate()));
            maxWeight.setText(Float.toString(mShip.ship.getMaxLoadKg()));

            if (mPorts != null) {

                int selectedPortPosition = 0;
                String[] portsNames = new String[mPorts.size()];

                for (int i = 0; i < portsNames.length; i++) {
                    portsNames[i] = mPorts.get(i).getName();

                    Log.v(TAG, "PA_Debug Spinner test, ship port: " + mShip.port.getName() + " port in list: " + mPorts.get(i).getName());
                    if (mPorts.get(i).getName().equals(mShip.port.getName())) {
                        selectedPortPosition = i;
                        Log.d(TAG, "PA_Debug Port from ship found in list " + mShip.port.getName());
                    }
                }

                portsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, portsNames);
                portsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                portsSpinner.setAdapter(portsAdapter);
                portsSpinner.setSelection(selectedPortPosition);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveShip();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }
}
