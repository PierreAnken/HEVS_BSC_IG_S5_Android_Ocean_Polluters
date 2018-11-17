package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class LogisticsManagerContainerViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;
    private ContainerViewModel mContainerViewModel;
    private ShipViewModel mShipModel;

    private ShipListViewModel mShipListModel;
    private ArrayAdapter<String> shipAdapter;
    private List<ShipWithContainer> mShips;

    private TextView dockPosition;
    private TextView containerName;
    private TextView shipNames;
    private TextView loadingStatus;
    private TextView items;
    private TextView weight;

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

        //add delete button
        LinearLayout containerViewPage = findViewById(R.id.lm_container_view_layout);
        View btnContainerManager = getLayoutInflater().inflate(R.layout.btn_container_manager, null);
        containerViewPage.addView(btnContainerManager);
        btnContainerManager.setOnClickListener(
                view -> {
                    openContainerManager(containerId);
                }
        );
    }

    private void openContainerManager(int containerId) {

        Intent containerView;

        containerView = new Intent(getApplicationContext(), LogisticsManagerContainerContentViewActivity.class);

        containerView.putExtra("containerId",Integer.toString(containerId));
        Log.d(TAG, "PA_Debug container id to edit:" + Integer.toString(containerId));
        startActivity(containerView);

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
            if (mContainerWithItem.container.getLoaded() == true) {
                loadingStatus.setText("loaded");
            } else {
                loadingStatus.setText("to load");
            }

            //get ship name from ship id
            ShipViewModel.FactoryShip factory = new ShipViewModel.FactoryShip(getApplication(), mContainerWithItem.container.getShipId());
            mShipModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
            mShipModel.getShip().observe(this, ship -> {
                if (ship != null) {
                    Log.d(TAG, "PA_Debug ship id from factory:" + ship.ship.getId());
                    shipNames.setText(ship.ship.getName());
                }
            });

        }

            items = findViewById(R.id.container_items_quantity_weight);
            weight = findViewById(R.id.t_total_weight);
            items.setText(mContainerWithItem.items.size() + " items");
            weight.setText(mContainerWithItem.getWeight() +" kg");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent itemAddEdit = new Intent(getApplicationContext(), LogisticsManagerItemAddEditActivity.class);
                itemAddEdit.putExtra("containerId",mContainerWithItem.container.getId().toString());
                Log.d(TAG, "PA_Debug container sent as intent to edit " + mContainerWithItem.container.getId());
                startActivity(itemAddEdit);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}