package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class ContainerWithItem {

    private static final String TAG = "ContainerWithItem";

    @Embedded
    public ContainerEntity container;

    @Relation(parentColumn = "e_container_id", entityColumn = "container_id", entity = ItemEntity.class)
    public List<ItemWithType> items;

    public int getWeight(){
        int weight = 0;

        if (items != null) {
            for (ItemWithType item : items) {
                weight += item.item.getWeightKg();
            }
        }
        Log.v(TAG, "PA_Debug getting weight of container " + container.getId() + " : " + weight);
        return weight;
    }

    public static List<ContainerWithItem> FillContainersFromSnap(DataSnapshot snapshotContainers){

        List<ContainerWithItem> containersW = new ArrayList<>();
        for(DataSnapshot containerSnap : snapshotContainers.getChildren()){

            ContainerWithItem containerW = new ContainerWithItem();
            containerW.container = containerSnap.getValue(ContainerEntity.class);
            containerW.items = ItemWithType.FillItemsFromSnap(containerSnap.child("items"));
            containersW.add(containerW);
        }
        return containersW;
    }

    public static ContainerWithItem FillContainerFromSnap(DataSnapshot snapshotContainer){

        ContainerWithItem containerW = new ContainerWithItem();
        containerW.container = snapshotContainer.getValue(ContainerEntity.class);
        containerW.items = ItemWithType.FillItemsFromSnap(snapshotContainer.child("items"));

        return containerW;
    }
}
