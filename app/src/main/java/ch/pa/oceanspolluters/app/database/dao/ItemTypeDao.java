package ch.pa.oceanspolluters.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public abstract class ItemTypeDao {


    @Query("SELECT * FROM itemTypes")
    public abstract List<ItemTypeEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(ItemTypeEntity itemType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ItemTypeEntity> itemTypes);

    @Update
    public abstract void update(ItemTypeEntity itemType);

    @Delete
    public abstract void delete(ItemTypeEntity itemType);

    @Query("DELETE FROM itemTypes")
    public abstract void deleteAll();
}
