package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.Item;

@Entity(tableName = "items",
        foreignKeys = {
                @ForeignKey(
                        entity = ContainerEntity.class,
                        parentColumns = "e_container_id", // remote class
                        childColumns = "container_id", // local class
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = ItemTypeEntity.class,
                        parentColumns = "e_item_type_id", // remote class
                        childColumns = "item_type_id" // local class
                )
        }
        ,
        indices = {
                @Index(value = {"container_id"}),
                @Index(value = {"item_type_id"})
        })

public class ItemEntity extends BaseEntity implements Item {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "e_item_id")
    private Integer id = null;

    @ColumnInfo(name = "container_id")
    private Integer containerId;

    @ColumnInfo(name = "weight_kg")
    private float weightKg;

    @ColumnInfo(name = "item_type_id")
    private int itemTypeId;

    @Ignore
    public ItemEntity(@NonNull Item item) {
        weightKg = item.getWeightKg();
        containerId = getContainerId();
    }

    public ItemEntity(int itemTypeId, float weightKg, int containerId) {
        this.weightKg = weightKg;
        this.containerId = containerId;
        this.itemTypeId = itemTypeId;
    }

    public int getItemTypeId() {
        return itemTypeId;
    }
    public void setItemTypeId(int itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){ this.id = id;}

    @Override
    public float getWeightKg() {
        return weightKg;
    }

    @Override
    public Integer getContainerId() {
        return containerId;
    }
    public void setWeightKg(float weightKg) {
        this.weightKg = weightKg;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ItemEntity)) return false;
        ItemEntity o = (ItemEntity) obj;
        return o.getId() == (this.getId());
    }

}