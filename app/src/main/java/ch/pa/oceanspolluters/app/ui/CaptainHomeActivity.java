package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;
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
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayShip(OperationMode.View, mShipsWithContainer.get(position).ship.getId());
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "PA_Debug long clicked position:" + position);
                DisplayShip(OperationMode.Edit, mShipsWithContainer.get(position).ship.getId());
            }
        }, ViewType.Captain_Home);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get ships from captain
        int idCaptain = ((BaseApp)getApplication()).getCurrentUser().getId();
        mShipsWithContainer = new ArrayList<>();

        ShipListViewModel.FactoryShips factory = new ShipListViewModel.FactoryShips(getApplication(), idCaptain);
        ShipListViewModel mShipsFromCaptain = ViewModelProviders.of(this, factory).get(ShipListViewModel.class);
        mShipsFromCaptain.getShips().observe(this, shipsWithContainer -> {
            if (shipsWithContainer != null) {
                mShipsWithContainer = shipsWithContainer;
                mAdapter.setData(mShipsWithContainer);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void DisplayShip(OperationMode mode, int shipId){

        Intent shipView;

        if(mode == OperationMode.View)
            shipView = new Intent(getApplicationContext(), CaptainShipViewActivity.class);
        else
            shipView = new Intent(getApplicationContext(), CaptainShipAddEditActivity.class);

        shipView.putExtra("shipId",Integer.toString(shipId));
        Log.d(TAG, "PA_Debug ship id to edit:" + Integer.toString(shipId));
        startActivity(shipView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                DisplayShip(OperationMode.Edit, -1);
                return true;
            case android.R.id.home:
                TB.ConfirmAction(this, getString(R.string.confirmDisconnect), () ->
                        {
                            this.finish();
                        }
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
