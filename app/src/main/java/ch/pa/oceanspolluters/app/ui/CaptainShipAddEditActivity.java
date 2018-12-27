package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.TB;

public class CaptainShipAddEditActivity extends AppCompatActivity {

    private ShipWithContainer mShip;
    private List<PortEntity> mPorts;

    private EditText shipName;
    private EditText maxWeight;
    private EditText departureDate;
    private Spinner portsSpinner;

    private static final String TAG = "CaptainShipAddEdit";
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_ship_add_edit);

        Intent shipDetail = getIntent();
        String shipIdFB = shipDetail.getStringExtra("shipIdFB");
        if(!shipIdFB.equals(""))
            setTitle(getString(R.string.ship_edit));
        else
            setTitle(getString(R.string.ship_add));

        shipName = findViewById(R.id.ae_ship_name);
        departureDate = findViewById(R.id.ae_departure_date);
        portsSpinner = findViewById(R.id.ae_destination_port_spinner);
        maxWeight = findViewById(R.id.ae_max_weight);

        Log.d(TAG, "PA_Debug received ship id from intent:" + shipIdFB);

        //get ship and display it
        Query shipQ = fireBaseDB.getReference("ships/"+shipIdFB);
        shipQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotShip) {

                mShip = ShipWithContainer.FillShipFromSnap(snapshotShip);

                Log.d(TAG, "PA_Debug ship id from FireBase:" +mShip.ship.getFB_Key());
                updateView();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading ship "+shipIdFB);
            }
        });

        Query portsQ = fireBaseDB.getReference("ports")
                .orderByChild("name");
        portsQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotPorts) {
                mPorts = new ArrayList<>();

                for(DataSnapshot port : snapshotPorts.getChildren()){
                    mPorts.add(port.getValue(PortEntity.class));
                }
                updateView();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading ship "+shipIdFB);
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
        String maxWeightS = maxWeight.getText().toString();
        float maxWeightF = 0;

        if(!TextUtils.isEmpty(maxWeightS )){
            maxWeightF = Float.parseFloat(maxWeightS);
            if (maxWeightF < 10000) {
                valid = false;
                maxWeight.setError(getString(R.string.error_invalid_date_format));
            }
        }else{
            valid = false;
            maxWeight.setError(getString(R.string.error_weight_empty));
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
                departureDate.setError(getString(R.string.error_invalid_date_format) + " : dd-mm-yyyy");
            }
        }

        if(valid){
            Log.d(TAG, "PA_Debug port selected: " + portsSpinner.getSelectedItem());
            int SelectedPort = 0;
            for (PortEntity port : mPorts) {
                if (port.getName().equals(portsSpinner.getSelectedItem())) {
                    break;
                }
                SelectedPort++;
            }
            //insert ship
            if(mShip.ship.getFB_Key() == null){
                mShip = new ShipWithContainer();
                mShip.ship = new ShipEntity(shipNameS, maxWeightF, ((BaseApp) getApplication()).getCurrentUser().getFB_Key(), mPorts.get(SelectedPort).getFB_Key(), convertedDate);
                mShip.ship.setFB_Key(fireBaseDB.getReference().child("ships").push().getKey());
                fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()).setValue(mShip.ship);

            }
            else{
                fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/name").setValue(shipNameS);
                fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/maxLoadKg").setValue(maxWeightF);
                fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/departureDate").setValue(convertedDate);
            }

            //we update port and captain anyway
            fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/captain").setValue(((BaseApp) getApplication()).getCurrentUser());
            fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/fb_captainId").setValue(((BaseApp) getApplication()).getCurrentUser().getFB_Key());
            fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/port").setValue(mPorts.get(SelectedPort));
            fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()+"/fb_destinationPortId").setValue( mPorts.get(SelectedPort).getFB_Key());

            finish();
        }
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");

        if(mShip.ship.getFB_Key() != null){
            shipName.setText(mShip.ship.getName());
            departureDate.setText(TB.getShortDate(mShip.ship.getDepartureDate()));
            maxWeight.setText(Float.toString(mShip.ship.getMaxLoadKg()));

        }

        if (mPorts != null) {

            int selectedPortPosition = 0;
            String[] portsNames = new String[mPorts.size()];

            for (int i = 0; i < portsNames.length; i++) {
                portsNames[i] = mPorts.get(i).getName();

                if (mShip.ship.getFB_Key() != null) {
                    if (mPorts.get(i).getName().equals(mShip.port.getName())) {
                        selectedPortPosition = i;
                        Log.d(TAG, "PA_Debug Port from ship found in list " + mShip.port.getName());
                    }
                }
            }

            ArrayAdapter<String> portsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, portsNames);
            portsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            portsSpinner.setAdapter(portsAdapter);
            portsSpinner.setSelection(selectedPortPosition);

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
