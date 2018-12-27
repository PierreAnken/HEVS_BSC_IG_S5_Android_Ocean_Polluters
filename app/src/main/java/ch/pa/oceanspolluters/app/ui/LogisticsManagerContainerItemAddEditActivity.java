package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;
import ch.pa.oceanspolluters.app.util.OnAsyncEventListener;
import ch.pa.oceanspolluters.app.util.OperationMode;

public class LogisticsManagerContainerItemAddEditActivity extends AppCompatActivity {

    private ItemWithType mItemWithType;
    private static final String TAG = "lmContItemAddEditAct";
    private TextView mItemWeight;
    private Spinner mSpinnerCategory;
    private String itemPathFB;
    private String containerIdFB;
    private List<ItemTypeEntity> mItemTypes;
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lm_container_item_add_edit);

        Intent containerDetail = getIntent();
        itemPathFB = containerDetail.getStringExtra("itemPathFB");
        containerIdFB = containerDetail.getStringExtra("containerIdFB");

        //get itemtype list
        ItemTypeEntity type = new ItemTypeEntity();
        type.setOperationMode(OperationMode.GetAll);

        //get types

            fireBaseDB.getReference("itemTypes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        mItemTypes = new ArrayList<>();
                        for(DataSnapshot itemType : dataSnapshot.getChildren()){
                            mItemTypes.add(itemType.getValue(ItemTypeEntity.class));
                        }
                        updateView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "PA_Debug error getting Types: "+databaseError);
                }
            });


        //we retrieve item
        if (itemPathFB.contains("items")) {

            fireBaseDB.getReference(itemPathFB).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        mItemWithType = ItemWithType.FillItemFromSnap(dataSnapshot);
                        updateView();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "PA_Debug error getting item: "+databaseError);
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

        int itemTypeIndex = 0;
        for (ItemTypeEntity itemType : mItemTypes) {
            if (itemType.getName().equals(itemTypeS)) {
                break;
            }
            itemTypeIndex++;
        }

        if (TextUtils.isEmpty(itemWeightS)) {
            mItemWeight.setError(getString(R.string.error_weight_empty));
        }
        else {

            float weight = Float.parseFloat(itemWeightS);

            if (mItemWithType == null) {
                mItemWithType = new ItemWithType();
                mItemWithType.item = new ItemEntity(mItemTypes.get(itemTypeIndex).getFB_Key(), weight, containerIdFB);
                itemPathFB+="/items/"+mItemWithType.item.getFB_Key();

                fireBaseDB.getReference("itemPathFB").setValue( mItemWithType.item);
                fireBaseDB.getReference(itemPathFB+"/fb_containerId").setValue(containerIdFB);
            }

            fireBaseDB.getReference(itemPathFB+"/itemType").setValue(mItemTypes.get(itemTypeIndex));
            fireBaseDB.getReference(itemPathFB+"/fb_itemTypeId").setValue(mItemTypes.get(itemTypeIndex).getFB_Key());
            fireBaseDB.getReference(itemPathFB+"/weightKg").setValue(weight);
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