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
import android.widget.ToggleButton;

import java.util.Collections;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;

public class LogisticsManagerContainerAddEditActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;
    private ContainerViewModel mContainerViewModel;
    private ShipListViewModel mShipListModel;
    private ArrayAdapter<String> shipAdapter;
    private List<ShipWithContainer> mShips;

    private EditText dockPosition;
    private EditText containerName;
    private Spinner shipNames;
    private ToggleButton loadingStatus;

    private static final String TAG = "lmContainerAddEditAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_add_edit);

        Intent containerDetail = getIntent();
        int containerId = containerDetail.getStringExtra("containerId") != null ? Integer.parseInt(containerDetail.getStringExtra("containerId")) : -1;
        Log.d(TAG, "PA_Debug received container id from intent:" + containerId);

        dockPosition = findViewById(R.id.ae_lm_dock_position);
        containerName = findViewById(R.id.ae_lm_container_name);
        shipNames = findViewById(R.id.ae_lm_ship_name_spinner);
        loadingStatus = findViewById(R.id.ae_lm_loaded_status);

        if (containerId >= 0) {
            setTitle(getString(R.string.container_edit));

            //get container and display it in form
            ContainerViewModel.FactoryContainer factory = new ContainerViewModel.FactoryContainer(getApplication(), containerId);
            mContainerViewModel = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
            mContainerViewModel.getContainer().observe(this, container -> {
                if (container != null) {
                    mContainerWithItem = container;
                    Log.d(TAG, "PA_Debug container id from factory:" + container.container.getId());
                    updateView();
                }
            });
        } else {
            setTitle(getString(R.string.container_add));

        }

        //get ship list for spinner
        ShipListViewModel.FactoryShips factoryShips = new ShipListViewModel.FactoryShips(getApplication(),-1);
        mShipListModel = ViewModelProviders.of(this, factoryShips).get(ShipListViewModel.class);
        mShipListModel.getShips().observe(this, ships -> {
            if (ships != null) {

                Collections.sort(ships, (a, b) -> a.ship.getName().compareTo(b.ship.getName()));

                mShips = ships;
                Log.d(TAG, "PA_Debug ships from factory:" + ships.size());
                updateView();
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
        Integer shipId = 0;


        for (ShipWithContainer ship : mShips) {
            if (ship.ship.getName().equals(shipName)) {
                shipId = ship.ship.getId();
                break;
            }
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

            ContainerEntity container = new ContainerEntity(containerNameS, dockPositionS, shipId, loaded);

            if (mContainerWithItem != null) {
                container.setId(mContainerWithItem.container.getId());
            }
            container.setOperationMode(OperationMode.Save);

            new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
                @Override
                public void onSuccess(List result) {
                    Log.d(TAG, "PA_Debug updateContainer: success");

                    if (container.getId() == null)
                        ((BaseApp) getApplication()).displayShortToast(getString(R.string.confirm_container_add));
                    else
                        ((BaseApp) getApplication()).displayShortToast(getString(R.string.confirm_container_save));
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "PA_Debug updateContainer: failure", e);
                    ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationFailled));
                    finish();
                }
            }).execute(container);
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
                    if (mShips.get(i).ship.getId() == mContainerWithItem.container.getShipId()) {
                        currentPosition = i;
                    }
                }
            }

            shipAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, shipsNames);
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
