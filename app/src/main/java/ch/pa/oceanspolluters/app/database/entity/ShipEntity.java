package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import ch.pa.oceanspolluters.app.model.Container;
import ch.pa.oceanspolluters.app.model.Item;
import ch.pa.oceanspolluters.app.model.Ship;

@Entity(tableName = "ships", primaryKeys = {"id"})
public class ShipEntity implements Ship {

    @NonNull
    private int id;

    private String name;

    @ColumnInfo(name = "capitain_id")
    private int capitainId;

    @ColumnInfo(name = "destination_port_id")
    private int destinationPortId;

    @ColumnInfo(name = "departure_date")
    private Date departureDate;

    @ColumnInfo(name = "max_Load_Kg")
    private float maxLoadKg;

    @ColumnInfo(name = "container_list")
    private ArrayList<Container> containerList;

    public ShipEntity() {
    }

    public ShipEntity(@NonNull Ship ship) {
        capitainId = ship.getCaptainId();
        destinationPortId = ship.getDestinationPortId();
        departureDate = ship.getDepartureDate();
        maxLoadKg = ship.getMaxLoad();
        containerList = new ArrayList<>(ship.getContainerList());
        name = ship.getName();
    }

    public ShipEntity(String name, float maxLoadKg) {
        this.name = name;
        this.maxLoadKg = maxLoadKg;
    }

    @Override
    public String getName(){return name;}
    public void setName(String name){this.name = name};

    @Override
    public int getId() {
        return id;
    }
    @Override
    public ArrayList<Container> getContainerList() {
        return containerList;
    }


    public void addContainer(@NonNull Container container) {
        containerList.add(container);
    }

    public void removeContainer(@NonNull Container container) {
        containerList.remove(container);
    }

    @Override
    public int getCaptainId() {
        return capitainId;
    }
    public void setCapitainId(int capitainId){this.capitainId = capitainId}


    @Override
    public int getDestinationPortId (){
        return destinationPortId;
    }

    public void setDestinationPortId(int destinationPortId) {
        this.destinationPortId = destinationPortId;
    }

    @Override
    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ShipEntity)) return false;
        ShipEntity o = (ShipEntity) obj;
        return o.getId() == (this.getId());
    }

}
