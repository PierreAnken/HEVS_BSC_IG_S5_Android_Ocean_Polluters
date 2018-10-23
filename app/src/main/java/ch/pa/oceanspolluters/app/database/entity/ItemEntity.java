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
                        parentColumns = "id", // remote class
                        childColumns = "container_id", // local class
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = {"container_id"})})
public class ItemEntity implements Item {

    @PrimaryKey(autoGenerate = true)
    private Integer id = null;
    private String name;

    @ColumnInfo(name = "container_id")
    private Integer containerId;

    @ColumnInfo(name = "weight_kg")
    private float weightKg;

    @Ignore
    public ItemEntity(@NonNull Item item) {
        name = item.getName();
        weightKg = item.getWeightKg();
        containerId = getContainerId();
    }

    public ItemEntity(String name, float weightKg, int id, int containerId) {
        this.name = name;
        this.weightKg = weightKg;
        this.containerId = containerId;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){ this.id = id;}

    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

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