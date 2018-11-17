package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.async.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.viewmodel.ContainerViewModel;
import ch.pa.oceanspolluters.app.viewmodel.ItemListViewModel;

public class LogisticsManagerItemAddEditActivity extends AppCompatActivity {

    private ContainerWithItem mContainerWithItem;
    private ContainerViewModel mContainerViewModel;
    private ItemListViewModel mItemListModel;
    private ArrayAdapter<String> itemAdapter;
    private List<ItemWithType> mItems;

    private TextView dockPosition;
    private TextView containerName;
    private TextView shipName;
    private TextView containerLoaded;
    private boolean containerLoadedB;
    private Spinner itemCategories;
    private EditText itemWeight;
    private int shipId;

    private static final String TAG = "lmItemAddEditAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_item_add_edit);

        Intent containerDetail = getIntent();
        int containerId = containerDetail.getStringExtra("containerId") != null ? Integer.parseInt(containerDetail.getStringExtra("containerId")): 0;

        if(containerId > 0)
            setTitle(getString(R.string.item_edit));
        else
            setTitle(getString(R.string.item_add));

        itemCategories = findViewById(R.id.ae_lm_item_category_spinner);
        itemWeight = findViewById(R.id.ae_lm_item_weight);
        shipName = findViewById(R.id.ae_lm_item_ship_name);
        dockPosition = findViewById(R.id.ae_lm_item_dock_position);
        containerName = findViewById(R.id.ae_lm_item_container_name);
        containerLoaded = findViewById(R.id.ae_lm_item_loaded_status);
        if (containerLoaded.getText().toString() == "loaded") {
            containerLoadedB = true;
        } else {
            containerLoadedB = false;
        }

        Log.d(TAG, "PA_Debug received container id from intent:" + containerId);

        //get container and display it in form
        ContainerViewModel.FactoryContainer factory = new ContainerViewModel.FactoryContainer(getApplication(), containerId);
        mContainerViewModel = ViewModelProviders.of(this, factory).get(ContainerViewModel.class);
        mContainerViewModel.getContainer().observe(this, container -> {
            if (container != null) {
                mContainerWithItem = container;
                shipId = container.container.getShipId();
                Log.d(TAG, "PA_Debug container id from factory:" + container.container.getId());
                updateView();
            }
        });

        //get port list
        ItemListViewModel.FactoryItems factoryItems = new ItemListViewModel.FactoryItems(getApplication(),containerId);
        mItemListModel = ViewModelProviders.of(this, factoryItems).get(ItemListViewModel.class);
        mItemListModel.getContainerItems().observe(this, items -> {
            if (items != null) {
                mItems = items;
                updateView();
            }
        });

    }

    private void saveItem(){
        boolean valid = true;

        Integer itemCategoriesX = (int)itemCategories.getSelectedItemId();
        String itemWeightX = itemWeight.getText().toString();

        if(TextUtils.isEmpty(itemWeightX)){
            valid = false;
            containerName.setError(getString(R.string.error_empty_item_weight));
        }

        if(valid){

            ContainerEntity container = new ContainerEntity(shipName.toString(),dockPosition.toString(),shipId,containerLoadedB);

            if (mContainerWithItem != null) {
                container.setId(mContainerWithItem.container.getId());
            }
            container.setOperationMode(OperationMode.Save);

            new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "PA_Debug updateContainer item: success");
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "PA_Debug updateContainer item: failure", e);
                    finish();
                }
            }).execute(container);
        }
    }

    private void updateView(){
        Log.d(TAG, "PA_Debug updateView");

        if(mItems != null){
            String[] categoryNames = new String[mItems.size()];

            for(int i = 0; i<categoryNames.length; i++){
                categoryNames[i] = mItems.get(i).itemTypes.get(0).getName();
            }

            itemAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryNames);
            itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            itemCategories.setAdapter(itemAdapter);
        }
        if(mContainerWithItem != null){
            itemWeight.setText(mContainerWithItem.items.get(0).item.getWeightKg() + " kg");
            itemCategories.setSelection(mContainerWithItem.container.getShipId());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveItem();
                return true;
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
    }
}
