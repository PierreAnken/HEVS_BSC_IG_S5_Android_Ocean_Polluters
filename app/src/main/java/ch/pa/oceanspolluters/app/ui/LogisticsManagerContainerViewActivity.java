package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.TB;

public class LogisticsManagerContainerViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;

    private ArrayAdapter<String> shipAdapter;
    private List<ShipWithContainer> mShips;
    private String containerPathFB;
    private TextView shipNames;

    private static final String TAG = "lmContainerViewAct";
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_view);

        Intent containerDetail = getIntent();
        containerPathFB = containerDetail.getStringExtra("containerPathFB");
        Log.d(TAG, "PA_Debug received container id from intent:" + containerPathFB);

        // get container and display it
        Query containerQ = fireBaseDB.getReference(containerPathFB)
                .orderByChild("name");

        containerQ.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotContainer) {
                if(snapshotContainer.exists()){
                    mContainerWithItem = ContainerWithItem.FillContainerFromSnap(snapshotContainer);
                    updateView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Error loading container:" + containerPathFB);
            }
        });
        //add edit button
        LinearLayout containerViewPage = findViewById(R.id.btn_layout_lm_container);
        View btnContainerManager = getLayoutInflater().inflate(R.layout.btn_container_manager, null);
        containerViewPage.addView(btnContainerManager);
        btnContainerManager.setOnClickListener(
                view -> {
                    openContainerManager();
                }
        );

        //add delete button
        View deleteButton = getLayoutInflater().inflate(R.layout.btn_delete_red, null);
        containerViewPage.addView(deleteButton);
        deleteButton.setOnClickListener(
                view -> {
                    confirmDelete();
                }
        );
    }

    private void openContainerManager() {

        Intent containerView;

        containerView = new Intent(getApplicationContext(), LogisticsManagerContainerContentViewActivity.class);

        containerView.putExtra("containerPathFB",containerPathFB);
        Log.d(TAG, "PA_Debug container id to edit:" + containerPathFB);
        startActivity(containerView);

    }

    private void confirmDelete() {
        TB.ConfirmAction(this, getString(R.string.sureDeleteContainer), () ->
                fireBaseDB.getReference(containerPathFB).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationSuccess));
                            finish();
                        }
                )
        );
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");

        if(mShips != null){
            String[] shipsNames = new String[mShips.size()];

            for(int i = 0; i<shipsNames.length; i++){
                shipsNames[i] = mShips.get(i).ship.getName();
            }
        }

        if(mContainerWithItem != null){
            TextView containerName = findViewById(R.id.container_name);
            TextView dockPosition = findViewById(R.id.container_dock_position);
            TextView loadingStatus = findViewById(R.id.v_lm_loaded_status);
            shipNames = findViewById(R.id.container_ship_name);
            containerName.setText(mContainerWithItem.container.getName());
            dockPosition.setText(mContainerWithItem.container.getDockPosition());
            if (mContainerWithItem.container.getLoaded()) {
                loadingStatus.setText("loaded");
            } else {
                loadingStatus.setText("to load");
            }

            fireBaseDB.getReference("ships/"+mContainerWithItem.container.getFB_shipId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotShip) {

                   if(snapshotShip.exists()){
                       ShipEntity ship = snapshotShip.getValue(ShipEntity.class);
                       shipNames.setText(ship.getName());
                   }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("PA_DEBUG : with loading all containers");
                }
            });
        }

        TextView items = findViewById(R.id.container_items_quantity_weight);
        TextView weight = findViewById(R.id.t_total_weight);
            items.setText(mContainerWithItem.items.size() + " items");
            weight.setText(mContainerWithItem.getWeight() +" kg");

    }

    private void AddEditContainer(String containerPathFB) {
        Intent containerView = new Intent(getApplicationContext(), LogisticsManagerContainerAddEditActivity.class);
        containerView.putExtra("containerPathFB", containerPathFB);
        Log.d(TAG, "PA_Debug container id to edit:" + containerPathFB);
        startActivity(containerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        if (mContainerWithItem != null) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
            case R.id.add:
                AddEditContainer(containerPathFB);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.delete:
                confirmDelete();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}