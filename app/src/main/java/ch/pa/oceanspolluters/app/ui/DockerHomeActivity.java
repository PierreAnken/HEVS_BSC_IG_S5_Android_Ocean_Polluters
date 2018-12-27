package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;

public class DockerHomeActivity extends AppCompatActivity {

    private static final String TAG = "DockerHomeActivity";

    private List<ShipWithContainer> mShipsWithContainer;
    private RecyclerAdapter<ShipWithContainer> mAdapter;
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

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
                    DisplayContainersToLoad(mShipsWithContainer.get(position).ship.getFB_Key());
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

        Query shipQ = fireBaseDB.getReference("ships");

        shipQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotShips) {

                mShipsWithContainer = new ArrayList<>();
                for (DataSnapshot shipSnapshot: snapshotShips.getChildren()) {
                    mShipsWithContainer.add(ShipWithContainer.FillShipFromSnap(shipSnapshot));
                }
                mShipsWithContainer.sort(Comparator.comparingInt(o -> (int) o.ship.getDepartureDate().getTime()));
                mAdapter.setData(mShipsWithContainer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void DisplayContainersToLoad(String shipIdFB) {

        Intent shipView;

        shipView = new Intent(getApplicationContext(), DockerShipContainerListActivity.class);

        shipView.putExtra("shipIdFB",shipIdFB);
        Log.d(TAG, "PA_Debug ship id to view:" + shipIdFB);
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
