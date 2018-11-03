package ch.pa.oceanspolluters.app.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ItemEntity;;


@Dao
public abstract class ItemDao {

    @Query("SELECT * FROM items WHERE e_item_id = :id")
    public abstract LiveData<ItemEntity> getByIdLD(int id);

    @Query("SELECT * FROM items WHERE container_id = :containerId")
    public abstract LiveData<List<ItemEntity>> getItemsFromContainerLD(int containerId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(ItemEntity port);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ItemEntity> items);

    @Update
    public abstract void update(ItemEntity port);

    @Delete
    public abstract void delete(ItemEntity port);

    @Query("DELETE FROM items")
    public abstract void deleteAll();
}
