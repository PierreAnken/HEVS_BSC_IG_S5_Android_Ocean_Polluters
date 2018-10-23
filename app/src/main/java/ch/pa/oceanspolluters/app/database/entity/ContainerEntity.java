package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.model.Item;

@Entity(tableName = "containers",
        foreignKeys =
        @ForeignKey(
                entity = ShipEntity.class,
                parentColumns = "id", // remote class
                childColumns = "ship_id", // local class
                onDelete = ForeignKey.SET_NULL
        ),

        indices = {
                @Index(
                        value = {"ship_id"}
                )}
)

public class ContainerEntity implements Container {

    @PrimaryKey(autoGenerate = true)
    private Integer id = null;

    @ColumnInfo(name = "dock_position")
    private String dockPosition;

    @ColumnInfo(name = "ship_id")
    private int shipId;

    private boolean loaded;


    @Ignore
    public ContainerEntity(@NonNull Container container) {
        dockPosition = container.getDockPosition();
        shipId = container.getShipId();
        loaded = container.getLoaded();
    }

    public ContainerEntity(String dockPosition, int shipId, boolean loaded, int id) {
        this.dockPosition = dockPosition;
        this.shipId = shipId;
        this.loaded = loaded;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){ this.id = id;}

    @Override
    public String getDockPosition() {
        return dockPosition;
    }

    public void setDockPosition(String dockPosition) {
        this.dockPosition = dockPosition;
    }

    @Override
    public int getShipId() {
        return shipId;
    }

    public void setShipId(int shipId) {
        this.shipId = shipId;
    }

    @Override
    public boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ContainerEntity)) return false;
        ContainerEntity o = (ContainerEntity) obj;
        return o.getId() == (this.getId());
    }

}
