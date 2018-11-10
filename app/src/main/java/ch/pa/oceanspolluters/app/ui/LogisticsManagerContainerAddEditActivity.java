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
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.async.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.PortListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

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
        int containerId = containerDetail.getStringExtra("containerId") != null ? Integer.parseInt(containerDetail.getStringExtra("containerId")): 0;

        if(containerId > 0)
            setTitle(getString(R.string.container_edit));
        else
            setTitle(getString(R.string.container_add));

        dockPosition = findViewById(R.id.ae_lm_dock_position);
        containerName = findViewById(R.id.ae_lm_container_name);
        shipNames = findViewById(R.id.ae_lm_ship_name_spinner);
        loadingStatus = findViewById(R.id.ae_lm_loaded_status);

        Log.d(TAG, "PA_Debug received container id from intent:" + containerId);

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

        //get port list
        ShipListViewModel.FactoryShips factoryShips = new ShipListViewModel.FactoryShips(getApplication(),-1);
        mShipListModel = ViewModelProviders.of(this, factoryShips).get(ShipListViewModel.class);
        mShipListModel.getShips().observe(this, ships -> {
            if (ships != null) {
                mShips = ships;
                updateView();
            }
        });

    }

    private void saveContainer(){
        boolean valid = true;

        containerName.setError(null);
        dockPosition.setError(null);

        String containerNameS = containerName.getText().toString();
        String dockPositionS = dockPosition.getText().toString();
        Boolean loaded = loadingStatus.isChecked();
        Integer shipId = (int)shipNames.getSelectedItemId();

        if(TextUtils.isEmpty(containerNameS)){
            valid = false;
            containerName.setError(getString(R.string.error_empty_container));
        }

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
                public void onSuccess() {
                    Log.d(TAG, "PA_Debug updateContainer: success");
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "PA_Debug updateContainer: failure", e);
                    finish();
                }
            }).execute(container);
        }
    }



    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");

        if(mShips != null){
            String[] shipsNames = new String[mShips.size()];

            for(int i = 0; i<shipsNames.length; i++){
                shipsNames[i] = mShips.get(i).ship.getName();
            }

            shipAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, shipsNames);
            shipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            shipNames.setAdapter(shipAdapter);
        }
        if(mContainerWithItem != null){
            containerName.setText(mContainerWithItem.container.getName());
            dockPosition.setText(mContainerWithItem.container.getDockPosition());
            loadingStatus.setChecked(mContainerWithItem.container.getLoaded());
            shipNames.setSelection(mContainerWithItem.container.getShipId());
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
