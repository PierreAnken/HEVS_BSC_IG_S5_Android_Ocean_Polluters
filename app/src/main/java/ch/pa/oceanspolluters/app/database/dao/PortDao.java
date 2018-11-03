package ch.pa.oceanspolluters.app.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.PortEntity;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public abstract class PortDao {

    @Query("SELECT * FROM ports WHERE e_port_id = :id")
    public abstract LiveData<PortEntity> getByIdLD(int id);

    @Query("SELECT * FROM ports WHERE e_port_id = :id")
    public abstract PortEntity getById(int id);

    @Query("SELECT * FROM ports ORDER BY port_name")
    public abstract LiveData<List<PortEntity>> getAllLD();

    @Query("SELECT * FROM ports ORDER BY port_name")
    public abstract List<PortEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(PortEntity port);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<PortEntity> ports);

    @Update
    public abstract void update(PortEntity port);

    @Delete
    public abstract void delete(PortEntity port);

    @Query("DELETE FROM ports")
    public abstract void deleteAll();
}
