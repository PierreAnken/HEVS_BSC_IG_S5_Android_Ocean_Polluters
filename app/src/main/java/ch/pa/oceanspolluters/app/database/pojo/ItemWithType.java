package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

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
}
