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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;

public class LogisticsManagerContainerContentViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItems;
    private RecyclerAdapter<ItemWithType> mAdapter;
    private String containerPathFB;
    private String containerIdFB;

    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    private static final String TAG = "lmContainerItemsViewAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_content_view);

        RecyclerView recyclerView = findViewById(R.id.lmContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayItem(position);
            }

            @Override
            public void onItemLongClick(int position) {
                confirmDelete(mContainerWithItems.items.get(position).item);
            }
        }, ViewType.LogMan_Container_Content_View);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        containerPathFB = getIntent().getStringExtra("containerPathFB");

        fireBaseDB.getReference(containerPathFB).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotContainer) {

                if(snapshotContainer.exists()){
                    mContainerWithItems = ContainerWithItem.FillContainerFromSnap(snapshotContainer);
                    containerIdFB = mContainerWithItems.container.getFB_Key();
                    mAdapter.setData(mContainerWithItems.items);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : with loading all containers");
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void deleteItem(ItemEntity item) {

        fireBaseDB.getReference(containerPathFB+"/items/"+item.getFB_Key()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "PA_Debug delete item: success");
                ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationSuccess));
            }
        });
    }

    private void confirmDelete(ItemEntity item) {
        TB.ConfirmAction(this, getString(R.string.sureDeleteItem), () ->
                {
                    deleteItem(item);
                }
        );
    }

    private void DisplayItem(int position) {

        Intent containerView;

        containerView = new Intent(getApplicationContext(), LogisticsManagerContainerItemAddEditActivity.class);

        containerView.putExtra("itemPathFB",containerPathFB+"/items/"+mContainerWithItems.items.get(position).item.getFB_Key());
        Log.d(TAG, "PA_Debug container id to edit:" + containerPathFB+"/items/"+mContainerWithItems.items.get(position).item.getFB_Key());
        startActivity(containerView);
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
                Intent itemAddEdit = new Intent(getApplicationContext(), LogisticsManagerContainerItemAddEditActivity.class);
                itemAddEdit.putExtra("itemPathFB", containerPathFB);
                itemAddEdit.putExtra("containerIdFB", containerIdFB);

                startActivity(itemAddEdit);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}