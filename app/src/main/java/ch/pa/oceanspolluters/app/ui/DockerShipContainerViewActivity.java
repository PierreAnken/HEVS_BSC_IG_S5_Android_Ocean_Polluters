package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class DockerShipContainerViewActivity extends AppCompatActivity {

    private ShipWithContainer mShipWithContainer;
    private ShipViewModel mContainerListViewModel;

    private TextView dockPosition;
    private TextView containerName;
    private TextView loadingStatus;
    private TextView dockerContainerListTitle;
    private int shipId;

    private static final String TAG = "dockerShipContViewAct";
    private RecyclerAdapter<ContainerWithItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docker_container_list);

        Intent shipDetail = getIntent();
        shipId = Integer.parseInt(shipDetail.getStringExtra("shipId"));

        //get ship and display it
        ShipViewModel.FactoryShip factory = new ShipViewModel.FactoryShip(getApplication(), shipId);
        mContainerListViewModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
        mContainerListViewModel.getShip().observe(this, ship -> {
            if (ship != null) {
                mShipWithContainer = ship;
                Log.d(TAG, "PA_Debug ship id from factory:" + ship.ship.getId());
                updateView();

            }
        });
    }

    private void updateView() {
        Log.d(TAG, "PA_Debug updateView");
        if(mShipWithContainer != null){
            ((TextView)findViewById(R.id.v_docker_container_code_name)).setText(mShipWithContainer.containers.get(1).container.getName());
            ((TextView)findViewById(R.id.v_docker_container_dock_position)).setText(mShipWithContainer.containers.get(1).container.getDockPosition());
            if (mShipWithContainer.containers.get(1).container.getLoaded() == true) {
                ((TextView)findViewById(R.id.v_docker_ship_container_loading_status)).setText("loaded");
            }
                ((TextView)findViewById(R.id.v_docker_ship_container_loading_status)).setText("to load");
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
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