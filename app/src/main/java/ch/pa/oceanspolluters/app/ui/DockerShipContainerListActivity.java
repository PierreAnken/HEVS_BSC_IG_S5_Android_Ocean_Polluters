package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
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
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class DockerShipContainerListActivity extends AppCompatActivity {

    private ShipViewModel mShipViewModel;
    private ShipWithContainer mShipWithContainer;
    private ContainerListViewModel mContainerListViewModel;
    private List<ContainerWithItem> mContainerWithItem;

    private String dockPosition;
    private String containerName;
    private String loadingStatus;
    private int shipId;

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
            }

            @Override
            public void onItemLongClick(View v, int position) {
            }
        }, ViewType.Docker_Ship_Container_List);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent shipDetail = getIntent();
        shipId = Integer.parseInt(shipDetail.getStringExtra("shipId"));

        Intent containerDetail = getIntent();
        int containerId = Integer.parseInt(containerDetail.getStringExtra("shipId"));
        Log.d(TAG, "OG_Debug received container id from intent:" + containerId);

        // get container and display it
        ContainerListViewModel.FactoryContainers factory = new ContainerListViewModel.FactoryContainers(getApplication(), containerId, true);
        mContainerListViewModel = ViewModelProviders.of(this, factory).get(ContainerListViewModel.class);
        mContainerListViewModel.getContainers().observe(this, cont -> {
            if (cont != null) {
                mContainerWithItem = cont;
                mAdapter.setData(mContainerWithItem);

//                if (mShipWithContainer.containers.get(1).container.getLoaded() == true) {
//                    ((TextView) findViewById(R.id.v_docker_ship_container_loading_status)).setText("loaded");
//                } else {
//                    ((TextView) findViewById(R.id.v_docker_ship_container_loading_status)).setText("to load");
//                }
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
                TextView title = findViewById(R.id.docker_container_list_title);
                Date currentTime = Calendar.getInstance().getTime();
                long diff = mShipWithContainer.ship.getDepartureDate().getTime()-currentTime.getTime();
                long diffSeconds = diff / 1000 % 60, diffMinutes = diff / (60 * 1000) % 60, diffHours = diff / (60 * 60 * 1000) % 24, diffDays = diff / (60 * 60 * 1000 * 24);
                title.setText(mShipWithContainer.ship.getName() + " / " + diffDays + " days, " + diffHours + " hours");
                int shipContainers = ship.containers.size();
                int containerLoaded = 0;

                for (ContainerWithItem container: ship.containers) {
                    if(container.container.getLoaded()){
                        containerLoaded++;
                    }
                }
                title.setText(mShipWithContainer.ship.getName() + " (" + diffDays + "d:" + diffHours + "h:" + diffMinutes + "m ) Loading: " + containerLoaded + " / "+ shipContainers);
                if (diffDays < 1) {
                    title.setTextColor(Color.parseColor("#FFA500"));
                    if (diffHours < 12) {
                        title.setTextColor(Color.RED);
                    }
                }
            }
        });
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