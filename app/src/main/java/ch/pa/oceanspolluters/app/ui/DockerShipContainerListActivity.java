package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class DockerShipContainerListActivity extends AppCompatActivity {

    private ShipViewModel mShipViewModel;
    private ShipWithContainer mShipWithContainer;
    private ContainerListViewModel mContainerListViewModel;
    private List<ContainerWithItem> mContainersWithItem;

    private String dockPosition;
    private String containerName;
    private String loadingStatus;


    private static final String TAG = "dockerShipContViewAct";
    private RecyclerAdapter<ContainerWithItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docker_ship_container_list);

        RecyclerView recyclerView = findViewById(R.id.dockerShipContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                TB.ConfirmAction(getParent(), getString(R.string.confirmLoaded), () -> {

                });
            }
            @Override
            public void onItemLongClick(View v, int position) {
            }
        }, ViewType.Docker_Ship_Container_list);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent shipDetail = getIntent();
        int shipId = Integer.parseInt(shipDetail.getStringExtra("shipId"));

        Intent containerDetail = getIntent();
        int containerId = Integer.parseInt(containerDetail.getStringExtra("shipId"));
        Log.d(TAG, "OG_Debug received container id from intent:" + containerId);

        // get the list of containers
        ContainerListViewModel.FactoryContainers factory = new ContainerListViewModel.FactoryContainers(getApplication(), shipId, true);
        mContainerListViewModel = ViewModelProviders.of(this, factory).get(ContainerListViewModel.class);
        mContainerListViewModel.getContainers().observe(this, contList -> {
            if (contList != null) {
                Log.d(TAG, "PA_Debug container received:" + contList.size());
                mContainersWithItem = contList;
                mAdapter.setData(mContainersWithItem);
            }
        });

        recyclerView.setAdapter(mAdapter);

        //get ship and display it in the header
        ShipViewModel.FactoryShip factory2 = new ShipViewModel.FactoryShip(getApplication(), shipId);
        mShipViewModel = ViewModelProviders.of(this, factory2).get(ShipViewModel.class);
        mShipViewModel.getShip().observe(this, ship -> {
            if (ship != null) {
                mShipWithContainer = ship;
                Log.d(TAG, "OG_Debug ship id from factory:" + ship.ship.getId());

                //calculate remaining time
                Date currentTime = Calendar.getInstance().getTime();
                long diff = ship.ship.getDepartureDate().getTime() - currentTime.getTime();
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (60 * 60 * 1000 * 24);

                StringBuilder remainingTime = new StringBuilder(ship.ship.getName() + " (");
                if (diffDays > 0)
                    remainingTime.append(diffDays + "d:");
                if (diffDays > 0 || diffHours > 0)
                    remainingTime.append(diffHours + "h:");

                remainingTime.append(diffMinutes + "m)");

                TextView title = findViewById(R.id.docker_container_list_title);
                title.setText(remainingTime.toString());

                //red title if short time
                if (diffDays < 1 && diffHours < 12) {
                    title.setBackgroundColor(Color.RED);
                }
            }
        });
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