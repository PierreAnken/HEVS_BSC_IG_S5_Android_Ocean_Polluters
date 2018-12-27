package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
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
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;

public class CaptainHomeActivity extends AppCompatActivity {

    private static final String TAG = "CaptainActivity";
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();
    private List<ShipWithContainer> mShipsWithContainer;
    private RecyclerAdapter<ShipWithContainer> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_home);

        RecyclerView recyclerView = findViewById(R.id.captainShipsRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayShip(OperationMode.View, mShipsWithContainer.get(position).ship.getFB_Key());
            }

            @Override
            public void onItemLongClick(int position) {
                Log.d(TAG, "PA_Debug long clicked position:" + position);
                DisplayShip(OperationMode.Edit, mShipsWithContainer.get(position).ship.getFB_Key());
            }
        }, ViewType.Captain_Home);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get ships from captain
        String idCaptainFB = ((BaseApp)getApplication()).getCurrentUser().getFB_Key();

        Query captainShipQ = fireBaseDB.getReference("ships")
                .orderByChild("fb_captainId")
                .equalTo(idCaptainFB);

        captainShipQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mShipsWithContainer = new ArrayList<>();
                for (DataSnapshot shipSnapshot: snapshot.getChildren()) {
                    mShipsWithContainer.add(ShipWithContainer.FillShipFromSnap(shipSnapshot));
                }
                mShipsWithContainer.sort(Comparator.comparingInt(o -> (int) o.ship.getDepartureDate().getTime()));
                mAdapter.setData(mShipsWithContainer);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading captain ships");
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void DisplayShip(OperationMode mode, String shipIdFB){

        Intent shipView;

        if(mode == OperationMode.View)
            shipView = new Intent(getApplicationContext(), CaptainShipViewActivity.class);
        else
            shipView = new Intent(getApplicationContext(), CaptainShipAddEditActivity.class);

        shipView.putExtra("shipIdFB",shipIdFB);
        Log.d(TAG, "PA_Debug ship id to edit:" + shipIdFB);
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
                DisplayShip(OperationMode.Edit, "");
                return true;
            case android.R.id.home:
                TB.ConfirmAction(this, getString(R.string.confirmDisconnect), this::finish);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
