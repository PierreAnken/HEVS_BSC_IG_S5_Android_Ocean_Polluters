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
import android.widget.TextView;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.ViewHolderDetails;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class LogisticsManagerContainerItemsViewActivity extends AppCompatActivity {

    private ShipViewModel mShipModel;
    private TextView shipNames;
    private ContainerWithItem mContainerWithItems;
    private RecyclerAdapter<ItemWithType> mAdapter;

    private static final String TAG = "lmContainerItemsViewAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_items_view);

        RecyclerView recyclerView = findViewById(R.id.lmContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayItem(OperationMode.View, position);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "PA_Debug long clicked position:" + position);
                DisplayItem(OperationMode.Edit, position);
            }
        }, ViewType.lmContainerItems, ViewHolderDetails.ItemtypeWeight);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        int containerId = Integer.parseInt(getIntent().getStringExtra("containerId"));
        ContainerViewModel.FactoryContainer factory = new ContainerViewModel.FactoryContainer(getApplication(), containerId);
        ContainerViewModel mAllContainers = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
        mAllContainers.getContainer().observe(this, containerWithItems -> {
            if (containerWithItems != null) {
                mContainerWithItems = containerWithItems;
                mAdapter.setData(mContainerWithItems.items);
            }
        });

        recyclerView.setAdapter(mAdapter);
    }

    private void DisplayItem(OperationMode mode, int itemId){

        Intent containerView;

        if(mode == OperationMode.View)
            containerView = new Intent(getApplicationContext(), LogisticsManagerContainerItemViewActivity.class);
        else // edit or create mode
            containerView = new Intent(getApplicationContext(), LogisticsManagerItemAddEditActivity.class);

        containerView.putExtra("itemId",Integer.toString(mContainerWithItems.items.get(0).item.getId()));
        containerView.putExtra("containerId",Integer.toString(itemId));
        Log.d(TAG, "PA_Debug container id to edit:" + Integer.toString(itemId));
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
                Intent itemAddEdit = new Intent(getApplicationContext(), LogisticsManagerItemAddEditActivity.class);
                itemAddEdit.putExtra("containerId", mContainerWithItems.items.get(0).item.getId().toString());
                Log.d(TAG, "PA_Debug item sent as intent to edit " + mContainerWithItems.items.get(0).item.getId());
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