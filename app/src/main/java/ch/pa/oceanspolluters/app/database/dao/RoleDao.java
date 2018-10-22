package ch.pa.oceanspolluters.app.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.RoleEntity;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public abstract class RoleDao {

    @Query("SELECT * FROM roles WHERE id = :id")
    public abstract LiveData<RoleEntity> getById(int id);

    @Query("SELECT * FROM roles")
    public abstract LiveData<List<RoleEntity>> getAll();

    @Insert
    public abstract long insert(RoleEntity role);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<RoleEntity> roles);

    @Update
    public abstract void update(RoleEntity role);

    @Delete
    public abstract void delete(RoleEntity role);

    @Query("DELETE FROM roles")
    public abstract void deleteAll();
}
