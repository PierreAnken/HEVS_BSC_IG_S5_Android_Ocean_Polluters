package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class ShipWithContainer {
    @Embedded
    public ShipEntity ship;

    @Relation(parentColumn = "e_ship_id", entityColumn = "ship_id", entity = ContainerEntity.class)
    public List<ContainerWithItem> containers;

    @Embedded
    public UserEntity captain;

    @Embedded
    public PortEntity port;

    public int getWeight(){
        int weight = 0;
        if(containers != null){
            for (ContainerWithItem container: containers
                    ) {
                weight+= container.getWeight();
            }
        }
        return weight;
    }


    public int containerToLoad() {
        int counter = 0;
        if (containers != null) {
            for (ContainerWithItem container : containers) {

                if (!container.container.getLoaded())
                    counter++;
            }
        }
        return counter;
    }

    public static ShipWithContainer FillShipFromSnap(DataSnapshot snapshotShip){

        ShipWithContainer shipWithContainer = new ShipWithContainer();

        shipWithContainer.ship = snapshotShip.getValue(ShipEntity.class);
        shipWithContainer.captain = snapshotShip.child("captain").getValue(UserEntity.class);
        shipWithContainer.port = snapshotShip.child("port").getValue(PortEntity.class);

        shipWithContainer.containers = ContainerWithItem.FillContainersFromSnap( snapshotShip.child("containers"));

        return shipWithContainer;
    }
}
