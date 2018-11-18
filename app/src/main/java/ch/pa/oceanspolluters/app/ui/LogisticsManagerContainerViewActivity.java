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

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class LogisticsManagerContainerViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;

    private ShipListViewModel mShipListModel;
    private ArrayAdapter<String> shipAdapter;
    private List<ShipWithContainer> mShips;

    private TextView shipNames;

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
        ContainerViewModel mContainerViewModel = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
        mContainerViewModel.getContainer().observe(this, cont -> {
            if (cont != null) {
                mContainerWithItem = cont;
                Log.d(TAG, "PA_Debug container id from factory:" + cont.container.getId());
                updateView();
            }
        });

        //add edit button
        LinearLayout containerViewPage = findViewById(R.id.btn_layout_lm_container);
        View btnContainerManager = getLayoutInflater().inflate(R.layout.btn_container_manager, null);
        containerViewPage.addView(btnContainerManager);
        btnContainerManager.setOnClickListener(
                view -> {
                    openContainerManager(containerId);
                }
        );

        //add delete button
        View deleteButton = getLayoutInflater().inflate(R.layout.btn_delete_red, null);
        containerViewPage.addView(deleteButton);
        deleteButton.setOnClickListener(
                view -> {
                    confirmDelete();
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

    private void confirmDelete() {
        TB.ConfirmAction(this, getString(R.string.sureDeleteContainer), () ->
                {
                    mContainerWithItem.container.setOperationMode(OperationMode.Delete);
                    new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
                        @Override
                        public void onSuccess(List result) {
                            ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationSuccess));
                            finish();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationFailled));
                        }
                    }).execute(mContainerWithItem.container);
                }
        );
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
            TextView containerName = findViewById(R.id.container_name);
            TextView dockPosition = findViewById(R.id.container_dock_position);
            TextView loadingStatus = findViewById(R.id.v_lm_loaded_status);
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
            ShipViewModel mShipModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
            mShipModel.getShip().observe(this, ship -> {
                if (ship != null) {
                    Log.d(TAG, "PA_Debug ship id from factory:" + ship.ship.getId());
                    shipNames.setText(ship.ship.getName());
                }
            });

        }
        TextView items = findViewById(R.id.container_items_quantity_weight);
        TextView weight = findViewById(R.id.t_total_weight);
            items.setText(mContainerWithItem.items.size() + " items");
            weight.setText(mContainerWithItem.getWeight() +" kg");

    }

    private void AddEditContainer(int containerId) {
        Intent containerView = new Intent(getApplicationContext(), LogisticsManagerContainerAddEditActivity.class);
        containerView.putExtra("containerId", Integer.toString(containerId));
        Log.d(TAG, "PA_Debug container id to edit:" + Integer.toString(containerId));
        startActivity(containerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (mContainerWithItem != null) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
            case R.id.add:
                AddEditContainer(mContainerWithItem != null ? mContainerWithItem.container.getId() : -1);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.delete:
                confirmDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}