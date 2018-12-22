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

    public void setContainerId(Integer containerId) {
        this.containerId = containerId;
    }

    @ColumnInfo(name = "container_id")
    private Integer containerId;

    public String getFB_containerId() {
        return FB_containerId;
    }

    public void setFB_containerId(String FB_containerId) {
        this.FB_containerId = FB_containerId;
    }

    @ColumnInfo(name = "FB_container_id")
    private String FB_containerId;

    @ColumnInfo(name = "weight_kg")
    private float weightKg;

    @ColumnInfo(name = "item_type_id")
    private int itemTypeId;

    public String getFB_itemTypeId() {
        return FB_itemTypeId;
    }

    public void setFB_itemTypeId(String FB_itemTypeId) {
        this.FB_itemTypeId = FB_itemTypeId;
    }

    @ColumnInfo(name = "FB_item_type_id")
    private String FB_itemTypeId;

    @Ignore
    public ItemEntity(@NonNull Item item) {
        weightKg = item.getWeightKg();
        containerId = getContainerId();
    }

    public ItemEntity(){};

    @Ignore
    public ItemEntity(int itemTypeId, float weightKg, int containerId) {
        this.weightKg = weightKg;
        this.containerId = containerId;
        this.itemTypeId = itemTypeId;
    }

    @Ignore
    public ItemEntity(String itemTypeIdFB, float weightKg, String containerIdFB) {
        this.weightKg = weightKg;
        this.FB_containerId = containerIdFB;
        this.FB_itemTypeId = itemTypeIdFB;
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