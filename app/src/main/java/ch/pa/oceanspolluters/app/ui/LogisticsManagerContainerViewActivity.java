package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class LogisticsManagerContainerViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;
    private ContainerViewModel mContainerViewModel;

    private ShipListViewModel mShipListModel;
    private ArrayAdapter<String> shipAdapter;
    private List<ShipWithContainer> mShips;

    private TextView dockPosition;
    private TextView containerName;
    private TextView shipNames;
    private ToggleButton loadingStatus;
    private TextView items;

    private static final String TAG = "lmContainerViewAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_view);

        Intent containerDetail = getIntent();
        int containerId = Integer.parseInt(containerDetail.getStringExtra("containerId"));
        Log.d(TAG, "PA_Debug received container id from intent:" + containerId);

        // get container and display it
        ContainerViewModel.FactoryContainer factory = new ContainerViewModel.FactoryContainer(getApplication(), containerId);
        mContainerViewModel = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
        mContainerViewModel.getContainer().observe(this, cont -> {
            if (cont != null) {
                mContainerWithItem = cont;
                Log.d(TAG, "PA_Debug container id from factory:" + cont.container.getId());
                updateView();
            }
        });
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");

        if(mShips != null){
            String[] shipsNames = new String[mShips.size()];

            for(int i = 0; i<shipsNames.length; i++){
                shipsNames[i] = mShips.get(i).ship.getName();
            }
        }

        if(mContainerWithItem != null){
            containerName = findViewById(R.id.container_name);
            dockPosition = findViewById(R.id.container_dock_position);
            loadingStatus = findViewById(R.id.v_lm_loaded_status);
            shipNames = findViewById(R.id.container_ship_name);
            containerName.setText(mContainerWithItem.container.getName());
            dockPosition.setText(mContainerWithItem.container.getDockPosition());
            loadingStatus.setChecked(mContainerWithItem.container.getLoaded());
            shipNames.setText(Integer.toString(mContainerWithItem.container.getShipId()));
        }

            items = findViewById(R.id.container_items_quantity_weight);
            items.setText(mContainerWithItem.getWeight() +"kg / "+ mContainerWithItem.items.size() + " items");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent containerAddEdit = new Intent(getApplicationContext(), LogisticsManagerContainerAddEditActivity.class);
                containerAddEdit.putExtra("containerId",mContainerWithItem.container.getId().toString());
                Log.d(TAG, "PA_Debug ship sent as intent to edit " + mContainerWithItem.container.getId());
                startActivity(containerAddEdit);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}