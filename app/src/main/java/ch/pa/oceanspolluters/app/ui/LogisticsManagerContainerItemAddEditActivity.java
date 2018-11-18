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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AsyncOperationOnEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;
import ch.pa.oceanspolluters.app.viewmodel.ItemViewModel;

public class LogisticsManagerContainerItemAddEditActivity extends AppCompatActivity {

    private ItemWithType mItemWithType;
    private static final String TAG = "lmContItemAddEditAct";
    private TextView mItemWeight;
    private Spinner mSpinnerCategory;

    private int containerId;
    private List<ItemTypeEntity> mItemTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_item_add_edit);

        Intent containerDetail = getIntent();
        int itemId = containerDetail.getStringExtra("itemId") != null ? Integer.parseInt(containerDetail.getStringExtra("itemId")) : -1;
        containerId = containerDetail.getStringExtra("containerId") != null ? Integer.parseInt(containerDetail.getStringExtra("containerId")) : -1;

        Log.d(TAG, "PA_Debug received item id from intent:" + itemId);

        //get itemtype list - we dont need live data as they don't change
        ItemTypeEntity type = new ItemTypeEntity();
        type.setOperationMode(OperationMode.GetAll);

        try {
            new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
                @Override
                public void onSuccess(List<ItemTypeEntity> result) {
                    Log.d(TAG, "PA_Debug success getting Types");
                    mItemTypes = result;
                    updateView();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "PA_Debug error getting Types:" + e);
                }

            }).execute(type);

        } catch (Exception e) {
            Log.d(TAG, "PA_Debug error getting Types:" + e);
        }


        //we retrieve item
        if (itemId >= 0) {
            ItemViewModel.FactoryItem factory = new ItemViewModel.FactoryItem(getApplication(), itemId);
            ItemViewModel mItemViewModel = ViewModelProviders.of(this, factory).get(ItemViewModel.class);
            mItemViewModel.getItem().observe(this, item -> {
                if (item != null) {
                    mItemWithType = item;
                    updateView();
                }
            });
        }


    }

    private void updateView() {
        //spinner generation
        mSpinnerCategory = findViewById(R.id.ae_lm_item_category_spinner);
        if (mItemTypes != null) {
            int selectedItemType = 0;
            String[] itemTypeNames = new String[mItemTypes.size()];

            for (int i = 0; i < itemTypeNames.length; i++) {
                itemTypeNames[i] = mItemTypes.get(i).getName();
                if (mItemWithType != null) {
                    if (mItemTypes.get(i).getName().equals(mItemWithType.itemType().getName())) {
                        selectedItemType = i;
                    }
                }
            }

            ArrayAdapter<String> itemTypeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, itemTypeNames);
            itemTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinnerCategory.setAdapter(itemTypeAdapter);
            mSpinnerCategory.setSelection(selectedItemType);
        }

        mItemWeight = findViewById(R.id.ae_lm_item_weight);

        if (mItemWithType != null) {
            float itemWeight = mItemWithType.item.getWeightKg();
            mItemWeight.setText(Float.toString(itemWeight));
        }

    }

    private void saveItem() {

        String itemTypeS = mSpinnerCategory.getSelectedItem().toString();
        String itemWeightS = mItemWeight.getText().toString();
        mItemWeight.setError(null);

        int itemTypeSelected = -1;

        for (ItemTypeEntity itemType : mItemTypes) {
            if (itemType.getName().equals(itemTypeS)) {
                itemTypeSelected = itemType.getId();
                break;
            }
        }
        Log.d(TAG, "PA_Debug item container id:" + containerId);

        if (TextUtils.isEmpty(itemWeightS)) {
            mItemWeight.setError(getString(R.string.error_weight_empty));
        }
        else {

            float weight = Float.parseFloat(itemWeightS);
            ItemEntity item = new ItemEntity(itemTypeSelected, weight, containerId);

            if (mItemWithType != null) {
                item.setId(mItemWithType.item.getId());
                Log.d(TAG, "PA_Debug updating item:" + item.getId());
            }

            item.setOperationMode(OperationMode.Save);
            new AsyncOperationOnEntity(getApplication(), new OnAsyncEventListener() {
                @Override
                public void onSuccess(List result) {
                    Log.d(TAG, "PA_Debug updateItem: success");
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    Log.d(TAG, "PA_Debug updateItem: failure", e);
                    finish();
                }
            }).execute(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ok, menu);
        return true;
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
}