package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;

public class DockerHomeActivity extends AppCompatActivity {

    private static final String TAG = "DockerHomeActivity";

    private List<ShipWithContainer> mShipsWithContainer;
    private RecyclerAdapter<ShipWithContainer> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docker_home);

        RecyclerView recyclerView = findViewById(R.id.dockerShipsRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                if (mShipsWithContainer.get(position).containerToLoad() > 0) {
                    DisplayContainersToLoad(mShipsWithContainer.get(position).ship.getId());
                } else {
                    ((BaseApp) getApplication()).displayShortToast(getString(R.string.fully_loaded));
                }
            }

            @Override
            public void onItemLongClick(int position) {
            }
        }, ViewType.Docker_Home);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get ships for docker
        mShipsWithContainer = new ArrayList<>();

        ShipListViewModel.FactoryShips factory = new ShipListViewModel.FactoryShips(getApplication(), -1);
        ShipListViewModel mShipsFromDocker = ViewModelProviders.of(this, factory).get(ShipListViewModel.class);
        mShipsFromDocker.getShips().observe(this, shipsWithContainer -> {
            if (shipsWithContainer != null) {
                mShipsWithContainer = shipsWithContainer;
                mAdapter.setData(mShipsWithContainer);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void DisplayContainersToLoad(int shipId) {

        Intent shipView;

        shipView = new Intent(getApplicationContext(), DockerShipContainerListActivity.class);

        shipView.putExtra("shipId",Integer.toString(shipId));
        Log.d(TAG, "PA_Debug ship id to view:" + Integer.toString(shipId));
        startActivity(shipView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                TB.ConfirmAction(this, getString(R.string.confirmDisconnect), this::finish
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
