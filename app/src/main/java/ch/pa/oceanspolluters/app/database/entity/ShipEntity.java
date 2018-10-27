package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import ch.pa.oceanspolluters.app.model.Ship;


@Entity(tableName = "ships",
        foreignKeys = {
                @ForeignKey(
                        entity = UserEntity.class,
                        parentColumns = "e_user_id", // remote class
                        childColumns = "captain_id", // local class
                        onDelete = ForeignKey.SET_NULL
                ),
                @ForeignKey(
                        entity = PortEntity.class,
                        parentColumns = "e_port_id", // remote class
                        childColumns = "destination_port_id", // local class
                        onDelete = ForeignKey.SET_NULL
                )
        },
        indices = {
                @Index(value = {"captain_id"}),
                @Index(value = {"destination_port_id"})}
)
public class ShipEntity implements Ship {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "e_ship_id")
    private Integer id = null;

    @ColumnInfo(name = "ship_name")
    private String name;

    @ColumnInfo(name = "captain_id")
    private int captainId;

    @ColumnInfo(name = "destination_port_id")
    private int destinationPortId;

    @ColumnInfo(name = "departure_date")
    private Date departureDate;

    @ColumnInfo(name = "max_Load_Kg")
    private float maxLoadKg;


    @Ignore
    public ShipEntity(@NonNull Ship ship) {
        captainId = ship.getCaptainId();
        destinationPortId = ship.getDestinationPortId();
        departureDate = ship.getDepartureDate();
        maxLoadKg = ship.getMaxLoadKg();
        name = ship.getName();
    }

    public ShipEntity(String name, float maxLoadKg, int captainId, int destinationPortId, Date departureDate) {
        this.name = name;
        this.maxLoadKg = maxLoadKg;
        this.captainId = captainId;
        this.destinationPortId = destinationPortId;
        this.departureDate = departureDate;
    }

    @Override
    public String getName(){return name;}

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){ this.id = id;}

    @Override
    public int getCaptainId() {
        return captainId;
    }

    public void setCaptainId(int capitainId) {
        this.captainId = capitainId;
    }

    @Override
    public float getMaxLoadKg() {
        return maxLoadKg;
    }

    public void setMaxLoadKg(float maxLoadKg) {
        this.maxLoadKg = maxLoadKg;
    }

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
