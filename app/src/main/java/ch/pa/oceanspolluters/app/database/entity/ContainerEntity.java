package ch.pa.oceanspolluters.app.database.entity;

import java.util.ArrayList;
import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;
import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.model.Item;

@Entity(tableName = "containers", primaryKeys = {"id"})
public class ContainerEntity implements Container {

    @NonNull
    private int id;

    @ColumnInfo(name = "item_list")
    private ArrayList<Item> itemList;

    @ColumnInfo(name = "dock_position")
    private String dockPosition;

    @ColumnInfo(name = "ship_id")
    private int  shipId;

    private boolean loaded;

    public ContainerEntity() {
    }

    public ContainerEntity(@NonNull Container container) {
        itemList = new ArrayList<>(container.getItemList());
        dockPosition = container.getDockPosition();
        shipId = container.getShipId();
        loaded = container.getLoadingStatus();
    }

    public ContainerEntity(String dockPosition, int shipId, boolean loaded) {
        this.dockPosition = dockPosition;
        this.shipId = shipId;
        this.loaded = loaded;
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void addItem(@NonNull Item item) {
        itemList.add(item);
    }

    public void removeItem(@NonNull Item item) {
        itemList.remove(item);
    }

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
    public boolean getLoadingStatus() {
        return loaded;
    }

    public void setLoadingStatus(boolean loaded) {
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
