package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.Item;

@Entity(tableName = "items", primaryKeys = {"id"})
public class ItemEntity implements Item {

    @NonNull
    private int id;
    private String name;

    @ColumnInfo(name = "weight_kg")
    private float weightKg;



    public ItemEntity() {
    }

    public ItemEntity(@NonNull Item item) {
        name = item.getName();
        weightKg = item.getWeightKg();
    }

    public ItemEntity(String name, float weightKg) {
        this.name = name;
        this.weightKg = weightKg;
    }

    @Override
    public int getId() {
        return id;
    }

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