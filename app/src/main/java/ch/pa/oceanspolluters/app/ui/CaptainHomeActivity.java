package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;

public class CaptainHomeActivity extends AppCompatActivity {

    private static final String TAG = "CaptainActivity";

    private List<ShipWithContainer> mShipsWithContainer;
    private RecyclerAdapter<ShipWithContainer> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_home);

        RecyclerView recyclerView = findViewById(R.id.captainShipsRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + mShipsWithContainer.get(position).ship.getName());

                Intent shipDetail = new Intent(getApplicationContext(), CaptainShipDetailActivity.class);
                shipDetail.putExtra("shipId",mShipsWithContainer.get(position).ship.getId());
                startActivity(shipDetail);
            }

            @Override
            public void onItemLongClick(View v, int position) {
            }
        });

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get ships from captain
        int idCaptain = ((BaseApp)getApplication()).getCurrentUser().getId();
        mShipsWithContainer = new ArrayList<>();

        ShipListViewModel.FactoryShips factory = new ShipListViewModel.FactoryShips(getApplication(), idCaptain);
        ShipListViewModel mShipsFromCaptain = ViewModelProviders.of(this, factory).get(ShipListViewModel.class);
        mShipsFromCaptain.getCaptainShips().observe(this, shipsWithContainer -> {
            if (shipsWithContainer != null) {
                mShipsWithContainer = shipsWithContainer;
                mAdapter.setData(mShipsWithContainer);
            }
        });
        recyclerView.setAdapter(mAdapter);
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
