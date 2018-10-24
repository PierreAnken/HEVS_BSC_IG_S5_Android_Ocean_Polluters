package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class ShipWithContainer {
    @Embedded
    public ShipEntity ship;

    @Relation(parentColumn = "e_ship_id", entityColumn = "ship_id", entity = ContainerEntity.class)
    public List<ContainerEntity> containers;

    @Embedded
    public UserEntity captain;

    @Embedded
    public PortEntity port;

}
