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

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.TB;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;

public class LogisticsManagerContainerContentViewActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItems;
    private RecyclerAdapter<ItemWithType> mAdapter;
    private int containerId;

    private static final String TAG = "lmContainerItemsViewAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_content_view);

        RecyclerView recyclerView = findViewById(R.id.lmContainersRecyclerView);

        mAdapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "PA_Debug clicked position:" + position);
                DisplayItem(OperationMode.Edit, position);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                confirmDelete(mContainerWithItems.items.get(position).item);
            }
        }, ViewType.LogMan_Container_Content_View);

        // generate new linear layout
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        containerId = Integer.parseInt(getIntent().getStringExtra("containerId"));
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

    private void deleteItem(ItemEntity item) {
        item.setOperationMode(OperationMode.Delete);

        new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
            @Override
            public void onSuccess(List result) {
                Log.d(TAG, "PA_Debug delete ship: success");
                ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationSuccess));

                Log.d(TAG, "PA_Debug mContainerWithItems.items.size: " + mContainerWithItems.items.size());
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "PA_Debug delete ship: failure", e);
                ((BaseApp) getApplication()).displayShortToast(getString(R.string.operationFailled));
            }
        }).execute(item);
    }

    private void confirmDelete(ItemEntity item) {
        TB.ConfirmAction(this, getString(R.string.sureDeleteItem), () ->
                {
                    deleteItem(item);
                }
        );
    }

    private void DisplayItem(OperationMode mode, int position){

        Intent containerView;

        containerView = new Intent(getApplicationContext(), LogisticsManagerContainerItemAddEditActivity.class);

        containerView.putExtra("itemId",Integer.toString(mContainerWithItems.items.get(position).item.getId()));
        containerView.putExtra("containerId",Integer.toString(containerId));
        Log.d(TAG, "PA_Debug container id to edit:" + Integer.toString(containerId));
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
                itemAddEdit.putExtra("containerId", mContainerWithItems.container.getId());
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