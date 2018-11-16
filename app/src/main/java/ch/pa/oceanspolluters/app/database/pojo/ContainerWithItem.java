package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.util.Log;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;

public class ContainerWithItem {

    private static final String TAG = "ContainerWithItem";

    @Embedded
    public ContainerEntity container;

    @Relation(parentColumn = "e_container_id", entityColumn = "container_id", entity = ItemEntity.class)
    public List<ItemEntity> items;

    public int getWeight(){

        Log.d(TAG, "PA_Debug getting weight of container "+container.getId());
        int weight = 0;
        if(items != null){
            for (ItemEntity item: items
                 ) {
                weight+= item.getWeightKg();
            }
        }
        return weight;
    }
}
