package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;

public class LogisticsManagerContainerAddEditActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;
    private List<ShipWithContainer> mShips;

    private EditText dockPosition;
    private EditText containerName;
    private Spinner shipNames;
    private ToggleButton loadingStatus;
    private String containerPathFB;
    private static final String TAG = "lmContainerAddEditAct";
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_add_edit);

        Intent containerDetail = getIntent();
        containerPathFB = containerDetail.getStringExtra("containerPathFB");

        dockPosition = findViewById(R.id.ae_lm_dock_position);
        containerName = findViewById(R.id.ae_lm_container_name);
        shipNames = findViewById(R.id.ae_lm_ship_name_spinner);
        loadingStatus = findViewById(R.id.ae_lm_loaded_status);

        //edit
        if (containerPathFB != null){
            setTitle(getString(R.string.container_edit));

            fireBaseDB.getReference(containerPathFB).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        mContainerWithItem = ContainerWithItem.FillContainerFromSnap(dataSnapshot);
                        updateView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "PA_Debug container id from factory:" + containerPathFB);
                }
            });

        } else {
            setTitle(getString(R.string.container_add));
        }

        //get ships for spinner
        fireBaseDB.getReference("ships").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mShips = new ArrayList<>();
                    for(DataSnapshot ship : dataSnapshot.getChildren()){
                        mShips.add(ShipWithContainer.FillShipFromSnap(ship));
                    }

                    mShips.sort(Comparator.comparing(o -> o.ship.getName()));
                    updateView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "PA_Debug error getting ships:" + databaseError);
            }
        });
    }


    private void saveContainer(){
        boolean valid = true;

        containerName.setError(null);
        dockPosition.setError(null);

        String containerNameS = containerName.getText().toString().toUpperCase();
        String dockPositionS = dockPosition.getText().toString();
        Boolean loaded = loadingStatus.isChecked();
        String shipName = shipNames.getSelectedItem().toString();

        Integer shipIndex = 0;
        for (ShipWithContainer ship : mShips) {
            if (ship.ship.getName().equals(shipName)) {
                break;
            }
            shipIndex++;
        }

        if(TextUtils.isEmpty(containerNameS)){
            valid = false;
            containerName.setError(getString(R.string.error_empty_container));
        }

        //test if container code is unique or the same as container we are editing
        for (ShipWithContainer ship : mShips) {
            for (ContainerWithItem container : ship.containers) {
                if (container.container.getName().equals(containerNameS)) {
                    if (mContainerWithItem != null) {
                        if (container.container.getId() != mContainerWithItem.container.getId()) {
                            valid = false;
                            break;
                        }
                    } else {
                        valid = false;
                        break;
                    }
                }
            }
        }
        if (!valid)
            containerName.setError(getString(R.string.error_duplicate_container_name));

        if(TextUtils.isEmpty(dockPositionS) && !loaded){
            valid = false;
            dockPosition.setError(getString(R.string.error_empty_dock_position));
        }

        if(valid){

            boolean addMode = mContainerWithItem == null;
            if (addMode) {
                containerPathFB = "ships/"+mShips.get(shipIndex).ship.getFB_Key()+"/containers";
                mContainerWithItem = new ContainerWithItem();
                mContainerWithItem.container = new ContainerEntity();
                mContainerWithItem.container.setFB_Key(fireBaseDB.getReference(containerPathFB).push().getKey());
                containerPathFB += "/"+mContainerWithItem.container.getFB_Key();
                fireBaseDB.getReference(containerPathFB).setValue(mContainerWithItem.container);
            }

            fireBaseDB.getReference(containerPathFB+"/dockPosition").setValue(dockPositionS);
            fireBaseDB.getReference(containerPathFB+"/fb_shipId").setValue(mShips.get(shipIndex).ship.getFB_Key());
            fireBaseDB.getReference(containerPathFB+"/loaded").setValue(loaded);
            fireBaseDB.getReference(containerPathFB+"/name").setValue(containerNameS);


            if (addMode)
                ((BaseApp) getApplication()).displayShortToast(getString(R.string.confirm_container_add));
            else
                ((BaseApp) getApplication()).displayShortToast(getString(R.string.confirm_container_save));

            finish();
        }
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");

        //set container data
        if (mContainerWithItem != null) {
            containerName.setText(mContainerWithItem.container.getName());
            dockPosition.setText(mContainerWithItem.container.getDockPosition());
            loadingStatus.setChecked(mContainerWithItem.container.getLoaded());
        }

        //set spinner data
        if(mShips != null){
            String[] shipsNames = new String[mShips.size()];

            int currentPosition = 0;
            for(int i = 0; i<shipsNames.length; i++){
                shipsNames[i] = mShips.get(i).ship.getName();
                if (mContainerWithItem != null) {
                    if (mShips.get(i).ship.getFB_Key().equals(mContainerWithItem.container.getFB_shipId())) {
                        currentPosition = i;
                        break;
                    }
                }
            }

            ArrayAdapter<String> shipAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, shipsNames);
            shipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            shipNames.setAdapter(shipAdapter);
            shipNames.setSelection(currentPosition);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveContainer();
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
