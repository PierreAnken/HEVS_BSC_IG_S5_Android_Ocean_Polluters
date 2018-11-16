package ch.pa.oceanspolluters.app.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.pojo.ItemWithType;


@Dao
public abstract class ItemDao {

    @Transaction
    @Query("SELECT * " +
            "FROM items")
    public abstract List<ItemWithType> getAll();

    @Transaction
    @Query("SELECT * " +
            "FROM items i " +
            "WHERE e_item_id = :id")
    public abstract ItemWithType getById(int id);

    @Transaction
    @Query("SELECT * " +
            "FROM items i " +
            "WHERE e_item_id = :id")
    public abstract LiveData<ItemWithType> getByIdLD(int id);

    @Transaction
    @Query("SELECT * " +
            "FROM items i " +
            "WHERE container_id = :containerId")
    public abstract LiveData<List<ItemWithType>> getItemsFromContainerLD(int containerId);

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
