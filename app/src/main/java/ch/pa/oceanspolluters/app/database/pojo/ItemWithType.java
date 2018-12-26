package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;

public class ItemWithType {
    @Embedded
    public ItemEntity item;

    @Relation(parentColumn = "item_type_id", entityColumn = "e_item_type_id", entity = ItemTypeEntity.class)
    public List<ItemTypeEntity> itemTypes;

    public ItemTypeEntity itemType() {
        if (itemTypes.size() > 0)
            return itemTypes.get(0);
        else
            return new ItemTypeEntity();
    }

    public static List<ItemWithType> FillItemsFromSnap(DataSnapshot snapshotItems){

            List<ItemWithType> itemsW = new ArrayList<>();
            for(DataSnapshot itemS : snapshotItems.getChildren()) {
                ItemWithType itemW = new ItemWithType();
                itemW.item = itemS.getValue(ItemEntity.class);
                itemW.itemTypes = new ArrayList<ItemTypeEntity>();
                itemW.itemTypes.add(itemS.child("itemType").getValue(ItemTypeEntity.class));

                itemsW.add(itemW);
            }

        return itemsW;
    }
}
