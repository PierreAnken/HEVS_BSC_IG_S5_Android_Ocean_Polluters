package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "itemTypes",
        foreignKeys =
        @ForeignKey(
                entity = ItemEntity.class,
                parentColumns = "e_item_type_id", // remote class
                childColumns = "item_type_id", // local class
                onDelete = ForeignKey.NO_ACTION
        )
        ,
        indices = {
                @Index(value = {"container_id"})
        })
public class ItemTypeEntity extends BaseEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "e_item_type_id")
    private Integer id = null;

    @ColumnInfo(name = "item_type")
    private String name;

    public ItemTypeEntity() {

    }

    @Ignore
    public ItemTypeEntity(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ItemTypeEntity)) return false;
        ItemTypeEntity o = (ItemTypeEntity) obj;
        return o.getId() == (this.getId());
    }

}