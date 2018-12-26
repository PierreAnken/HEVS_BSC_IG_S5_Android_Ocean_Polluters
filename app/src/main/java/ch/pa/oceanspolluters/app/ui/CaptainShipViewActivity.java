package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class CaptainShipViewActivity extends AppCompatActivity {

    private ShipWithContainer mShip;
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    private static final String TAG = "CaptainShipView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_ship_view);

        Intent shipDetail = getIntent();
        String shipIdFB = shipDetail.getStringExtra("shipIdFB");

        //get ship and display it

        Query shipQ = fireBaseDB.getReference("ships/"+shipIdFB);

        shipQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotShip) {

                if(snapshotShip.exists()){
                    mShip = ShipWithContainer.FillShipFromSnap(snapshotShip);

                    Log.d(TAG, "PA_Debug ship id from FireBase:" +mShip.ship.getFB_Key());
                    updateView();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading ship "+shipIdFB);
            }
        });

        //add delete button
        LinearLayout shipViewPage = findViewById(R.id.cap_ship_detail);
        View deleteButton = getLayoutInflater().inflate(R.layout.btn_delete_red, null);
        shipViewPage.addView(deleteButton);
        deleteButton.setOnClickListener(
                view -> {
                    confirmDelete();
                }
        );
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");
        if(mShip != null){
            ((TextView)findViewById(R.id.t_ship_name)).setText(mShip.ship.getName());
            ((TextView)findViewById(R.id.t_destination_port)).setText(mShip.port.getName());
            ((TextView)findViewById(R.id.t_departure_date)).setText(TB.getShortDate(mShip.ship.getDepartureDate()));

            int containerLoaded = 0;
            int totalLoadedWeight = 0;
            int shipContainers = 0;

            if(mShip.containers != null)
                shipContainers = mShip.containers.size();

            for (ContainerWithItem container: mShip.containers
                 ) {
                if(container.container.getLoaded()){
                    containerLoaded++;
                    totalLoadedWeight+= container.getWeight();
                }
            }
            ((TextView)findViewById(R.id.t_container_loaded)).setText(containerLoaded+"/"+shipContainers);

            TextView weightInfo = findViewById(R.id.t_total_weight);
            weightInfo.setText(totalLoadedWeight+"/"+mShip.ship.getMaxLoadKg()+ " kg");

            if(totalLoadedWeight > mShip.ship.getMaxLoadKg()){
                weightInfo.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.Red));
            }

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if(mShip != null){
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }


    private void deleteShip() {


        fireBaseDB.getReference("ships/"+mShip.ship.getFB_Key()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "PA_Debug delete ship: success");
                ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationSuccess));
                finish();
            }
        });

    }

    private void confirmDelete(){
        TB.ConfirmAction(this, getString(R.string.sureDeleteShip), () ->
                {
                    deleteShip();
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent shipAddEdit = new Intent(getApplicationContext(), CaptainShipAddEditActivity.class);
                shipAddEdit.putExtra("shipIdFB",mShip.ship.getFB_Key());
                Log.d(TAG, "PA_Debug ship sent as intent to edit "+mShip.ship.getId());
                startActivity(shipAddEdit);
                return true;
            case R.id.delete:
                confirmDelete();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
