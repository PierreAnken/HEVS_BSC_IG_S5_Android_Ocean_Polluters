package ch.pa.oceanspolluters.app.database.pojo;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.model.Container;

public class ContainerWithItem {
    @Embedded
    public ContainerEntity container;

    @Relation(parentColumn = "e_container_id", entityColumn = "container_id", entity = ItemEntity.class)
    public List<ItemEntity> items;
}
