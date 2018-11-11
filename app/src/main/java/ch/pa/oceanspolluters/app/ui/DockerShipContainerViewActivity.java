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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class DockerShipContainerViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;
    private ContainerViewModel mContainerListViewModel;

    private ShipViewModel mViewModel;
    private ShipWithContainer mShip;

    private TextView dockPosition;
    private TextView containerName;
    private TextView loadingStatus;
    private TextView dockerContainerListTitle;
    private List<ContainerWithItem> mShipWithContainer;

    private static final String TAG = "dockerShipContViewAct";
    private RecyclerAdapter<ContainerWithItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docker_container_list);

        Intent shipDetail = getIntent();
        int shipId = Integer.parseInt(shipDetail.getStringExtra("shipId"));

        //get ship and display it
        ShipViewModel.FactoryShip factory = new ShipViewModel.FactoryShip(getApplication(), shipId);
        mViewModel = ViewModelProviders.of(this, factory).get(ShipViewModel.class);
        mViewModel.getShip().observe(this, ship -> {
            if (ship != null) {
                mShip = ship;
                Log.d(TAG, "PA_Debug ship id from factory:" + ship.ship.getId());
                updateView();

            }
        });

        RecyclerView recyclerView = findViewById(R.id.dockerShipContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
            }

            @Override
            public void onItemLongClick(View v, int position) {
            }
        });

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TextView shipName = findViewById(R.id.docker_container_list_title);
        shipName.setText("Test");

//        ContainerListViewModel.FactoryContainers factory2 = new ContainerListViewModel.FactoryContainers(getApplication(), mShip.ship.getId());
//        ContainerListViewModel mContainerForDocker = ViewModelProviders.of(this, factory2).get(ContainerListViewModel.class);
//        mContainerForDocker.getContainers().observe(this, cont -> {
//            if (cont != null) {
//                mShipWithContainer = cont;
//                mAdapter.setData(mShipWithContainer);
//            }
//        });
        recyclerView.setAdapter(mAdapter);
    }

    private void updateView() {
        Log.d(TAG, "PA_Debug updateView");

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