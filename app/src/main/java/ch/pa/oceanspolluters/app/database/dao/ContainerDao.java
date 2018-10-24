package ch.pa.oceanspolluters.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;

;


@Dao
public abstract class ContainerDao {

    @Query("SELECT * FROM containers WHERE e_container_id = :id")
    public abstract ContainerEntity getById(int id);

    @Query("SELECT * FROM containers")
    public abstract List<ContainerEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(ContainerEntity container);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ContainerEntity> containers);

    @Update
    public abstract void update(ContainerEntity container);

    @Delete
    public abstract void delete(ContainerEntity container);

    @Query("DELETE FROM containers")
    public abstract void deleteAll();
}
