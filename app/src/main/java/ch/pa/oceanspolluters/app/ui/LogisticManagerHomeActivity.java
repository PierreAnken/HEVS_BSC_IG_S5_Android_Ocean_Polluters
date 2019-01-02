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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;

public class LogisticManagerHomeActivity extends AppCompatActivity {

    private static final String TAG = "lmHomeActivity";

    private List<ContainerWithItem> mContainerWithItems;
    private RecyclerAdapter<ContainerWithItem> mAdapter;
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_home);

        RecyclerView recyclerView = findViewById(R.id.lmContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayContainer(OperationMode.View, mContainerWithItems.get(position).container);
            }

            @Override
            public void onItemLongClick(int position) {
                Log.d(TAG, "PA_Debug long clicked position:" + position);
                DisplayContainer(OperationMode.Edit, mContainerWithItems.get(position).container);
            }
        }, ViewType.LogMan_Home);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get all containers
        fireBaseDB.getReference("ships").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotShips) {

                mContainerWithItems = new ArrayList<>();

                if(snapshotShips.exists()){
                    for(DataSnapshot shipS : snapshotShips.getChildren()){
                        ShipWithContainer ship = ShipWithContainer.FillShipFromSnap(shipS);
                        mContainerWithItems.addAll(ship.containers);
                    }

                    mContainerWithItems.sort((o1, o2) -> o1.container.getName().compareToIgnoreCase(o2.container.getName()));
                }

                mAdapter.setData(mContainerWithItems);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading all containers");
            }
        });

        recyclerView.setAdapter(mAdapter);
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
                DisplayContainer(OperationMode.Edit, null);
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

    private void DisplayContainer(OperationMode mode, ContainerEntity container){

        Intent containerView;

        if(mode == OperationMode.View)
            containerView = new Intent(getApplicationContext(), LogisticsManagerContainerViewActivity.class);
        else // edit or create mode
            containerView = new Intent(getApplicationContext(), LogisticsManagerContainerAddEditActivity.class);
        if(container != null){
            containerView.putExtra("containerPathFB","ships/"+container.getFB_shipId()+"/containers/"+container.getFB_Key());
            Log.d(TAG, "PA_Debug container id to edit:" +container.getFB_Key());
        }
        startActivity(containerView);
    }

}