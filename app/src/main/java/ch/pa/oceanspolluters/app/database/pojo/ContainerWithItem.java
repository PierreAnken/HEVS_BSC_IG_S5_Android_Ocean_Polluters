package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;

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
        return weight;
    }

    public static List<ContainerWithItem> FillContainersFromSnap(DataSnapshot snapshotContainers){

        List<ContainerWithItem> containersW = new ArrayList<>();
        for(DataSnapshot containerSnap : snapshotContainers.getChildren()){
            containersW.add(FillContainerFromSnap(containerSnap));
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
