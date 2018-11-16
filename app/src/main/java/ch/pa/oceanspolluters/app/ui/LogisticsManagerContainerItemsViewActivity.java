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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.ViewHolderDetails;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ItemListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class LogisticsManagerContainerItemsViewActivity extends AppCompatActivity {

    private ShipViewModel mShipModel;
    private TextView shipNames;
    private ContainerWithItem mContainerWithItems;
    private RecyclerAdapter<ContainerWithItem> mAdapter;

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
                DisplayContainer(OperationMode.View, mContainerWithItems.container.getId());
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "PA_Debug long clicked position:" + position);
                DisplayContainer(OperationMode.Edit, mContainerWithItems.container.getId());
            }
        }, ViewType.lmContainerItems, ViewHolderDetails.ItemtypeWeight);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ContainerViewModel.FactoryContainer factory = new ContainerViewModel.FactoryContainer(getApplication(), -1);
        ContainerViewModel mAllContainers = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
        mAllContainers.getContainer().observe(this, containerWithItems -> {
            if (containerWithItems != null) {
                mContainerWithItems = containerWithItems;
                mAdapter.setData(mContainerWithItems);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void DisplayContainer(OperationMode mode, int containerId){

        Intent containerView;

        if(mode == OperationMode.View)
            containerView = new Intent(getApplicationContext(), LogisticsManagerContainerViewActivity.class);
        else // edit or create mode
            containerView = new Intent(getApplicationContext(), LogisticsManagerContainerAddEditActivity.class);

        containerView.putExtra("containerId",Integer.toString(containerId));
        Log.d(TAG, "PA_Debug container id to edit:" + Integer.toString(containerId));
        startActivity(containerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent containerAddEdit = new Intent(getApplicationContext(), LogisticsManagerContainerAddEditActivity.class);
                containerAddEdit.putExtra("containerId",mContainerWithItem.container.getId().toString());
                Log.d(TAG, "PA_Debug ship sent as intent to edit " + mContainerWithItem.container.getId());
                startActivity(containerAddEdit);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}