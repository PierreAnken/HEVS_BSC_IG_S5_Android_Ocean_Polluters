package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.ViewHolderDetails;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;

public class LogisticManagerHomeActivity extends AppCompatActivity {

    private static final String TAG = "lmHomeActivity";

    private List<ContainerWithItem> mContainerWithItems;
    private RecyclerAdapter<ContainerWithItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistic_manager_home);

        RecyclerView recyclerView = findViewById(R.id.lmContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayContainer(OperationMode.View, mContainerWithItems.get(position).container.getId());
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "PA_Debug long clicked position:" + position);
                DisplayContainer(OperationMode.Edit, mContainerWithItems.get(position).container.getId());
            }
        }, ViewType.lmHome, ViewHolderDetails.ContainernameWeight);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // get all containers
        mContainerWithItems = new ArrayList<>();

        ContainerListViewModel.FactoryContainers factory = new ContainerListViewModel.FactoryContainers(getApplication(), -1, false);
        ContainerListViewModel mAllContainers = ViewModelProviders.of(this, factory).get(ContainerListViewModel.class);
        mAllContainers.getContainers().observe(this, containerWithItems -> {
            if (containerWithItems != null) {
                mContainerWithItems = containerWithItems;
                mAdapter.setData(mContainerWithItems);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                DisplayContainer(OperationMode.Save, -1);
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}