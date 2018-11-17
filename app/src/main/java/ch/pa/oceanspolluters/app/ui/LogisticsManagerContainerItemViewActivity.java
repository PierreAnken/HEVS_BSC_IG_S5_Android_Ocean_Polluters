package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.adapter.RecyclerAdapter;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.ViewHolderDetails;
import ch.pa.oceanspolluters.app.util.ViewType;
import ch.pa.oceanspolluters.app.viewmodel.ContainerListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ItemListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ItemViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipListViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ShipViewModel;

public class LogisticsManagerContainerItemViewActivity extends AppCompatActivity {

    private ItemWithType mItemWithType;
    private ContainerWithItem mContainerWithItems;
    private ItemViewModel mItemViewModel;
    private ContainerViewModel mContainerViewModel;
    private ShipViewModel mShipViewModel;

    private ArrayAdapter<String> itemAdapter;
    private ContainerWithItem mContainerItems;

    private TextView dockPosition;
    private TextView containerName;
    private TextView shipNames;
    private TextView loadingStatus;
    private TextView items;

    private int itemId;
    private int containerId;

    private static final String TAG = "lmContainerItemViewAct";
    private RecyclerAdapter<ContainerWithItem> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_item_view);

        Intent containerDetail = getIntent();
        itemId = Integer.parseInt(containerDetail.getStringExtra("itemId"));
        containerId = Integer.parseInt(containerDetail.getStringExtra("containerId"));
        Log.d(TAG, "PA_Debug received container for items id from intent:" + itemId);

        ItemViewModel.FactoryItem factory2 = new ItemViewModel.FactoryItem(getApplication(), itemId);
        mItemViewModel = ViewModelProviders.of(this, factory2).get(ItemViewModel.class);
        mItemViewModel.getItem().observe(this, itemx -> {
            if (itemx != null) {
                mItemWithType = itemx;
                Log.d(TAG, "PA_Debug item id from factory:" + itemx.itemType().getName());
            }

        });

        // get container and display it
        ContainerViewModel.FactoryContainer factory = new ContainerViewModel.FactoryContainer(getApplication(), containerId);
        mContainerViewModel = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
        mContainerViewModel.getContainer().observe(this, cont -> {
            if (cont != null) {
                mContainerItems = cont;
                Log.d(TAG, "PA_Debug container id from factory:" + cont.container.getId());
            }

        });

//        //get ship name from ship id
//        ShipViewModel.FactoryShip factory3 = new ShipViewModel.FactoryShip(getApplication(), mContainerItems.container.getShipId());
//        mShipViewModel = ViewModelProviders.of(this, factory3).get(ShipViewModel.class);
//        mShipViewModel.getShip().observe(this, ship -> {
//            if (ship != null) {
//                Log.d(TAG, "PA_Debug ship id from factory:" + mContainerItems.container.getShipId());
//                shipNames.setText(ship.ship.getName());
//            }
//        });

        if(mItemWithType != null){
            containerName = findViewById(R.id.v_lm_item_view_container_name);
            dockPosition = findViewById(R.id.v_lm_item_view_dock_position);
            loadingStatus = findViewById(R.id.v_lm_item_view_loaded_status);
            shipNames = findViewById(R.id.v_lm_item_view_view_ship_name);
            shipNames.setText(mContainerWithItems.container.getShipId());
            containerName.setText(mContainerItems.container.getName());
            dockPosition.setText(mContainerItems.container.getDockPosition());
            loadingStatus.setText(mContainerItems.container.getLoaded() + " ");

        }

        items = findViewById(R.id.v_lm_item_view_category_tf);
        items.setText(mItemWithType.itemType().getName());
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
                containerAddEdit.putExtra("itemId",mItemWithType.item.getId().toString());
                Log.d(TAG, "PA_Debug item sent as intent to edit " + mItemWithType.item.getId());
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