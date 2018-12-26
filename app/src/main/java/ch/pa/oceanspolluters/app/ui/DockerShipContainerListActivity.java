package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class DockerShipContainerListActivity extends AppCompatActivity {


    private List<ContainerWithItem> mContainersWithItem;
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();
    private ShipWithContainer mShipWithContainer;

    private static final String TAG = "dockerShipContViewAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docker_ship_container_list);

        RecyclerView recyclerView = findViewById(R.id.dockerShipContainersRecyclerView);
        AppCompatActivity appCompatActivity = this;

        RecyclerAdapter<ContainerWithItem> mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "PA_Debug clicked on:" + position);

                TB.ConfirmAction(appCompatActivity, getString(R.string.confirmLoaded), () -> {
                    ContainerEntity container = mContainersWithItem.get(position).container;
                    fireBaseDB.getReference("ships/" + mShipWithContainer.ship.getFB_Key() + "/containers/" + container.getFB_Key() + "/loaded")
                            .setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ((BaseApp) getApplication()).displayShortToast(getString(R.string.toast_container_loaded));
                        }
                    });
                });
            }

            @Override
            public void onItemLongClick(int position) {
            }
        }, ViewType.Docker_Ship_Container_list);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent shipDetail = getIntent();
        String shipIdFB = shipDetail.getStringExtra("shipIdFB");

        //get ship and display it in the header
        Query shipQ = fireBaseDB.getReference("ships/"+shipIdFB);
        shipQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotShip) {

                if(snapshotShip.exists()){
                    mShipWithContainer = ShipWithContainer.FillShipFromSnap(snapshotShip);

                    if (mShipWithContainer.ship.getFB_Key() != null) {

                        if (mShipWithContainer.containerToLoad() == 0){
                            ((BaseApp) getApplication()).displayShortToast(getString(R.string.fully_loaded));
                            finish();
                        }

                        //calculate remaining time
                        Date currentTime = Calendar.getInstance().getTime();
                        long diff = mShipWithContainer.ship.getDepartureDate().getTime() - currentTime.getTime();
                        if (diff < 0)
                            diff = 0;
                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        long diffDays = diff / (60 * 60 * 1000 * 24);

                        StringBuilder remainingTime = new StringBuilder(mShipWithContainer.ship.getName() + " (");
                        if (diffDays > 0)
                            remainingTime.append(diffDays).append("d:");
                        if (diffDays > 0 || diffHours > 0)
                            remainingTime.append(diffHours).append("h:");

                        remainingTime.append(diffMinutes).append("m)");

                        TextView title = findViewById(R.id.docker_container_list_title);
                        title.setText(remainingTime.toString());

                        //red title if short time
                        if (diffDays < 1 && diffHours < 12) {
                            title.setBackgroundColor(Color.RED);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading ship "+shipIdFB);
            }
        });

        // get the list of containers
        Query containersQ = fireBaseDB.getReference("ships/"+shipIdFB+"/containers")
                .orderByChild("loaded")
                .equalTo(false);
        containersQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotContainers) {

                if(snapshotContainers.exists()){
                    mContainersWithItem = ContainerWithItem.FillContainersFromSnap(snapshotContainers);
                    mAdapter.setData(mContainersWithItem);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading ship "+shipIdFB);
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