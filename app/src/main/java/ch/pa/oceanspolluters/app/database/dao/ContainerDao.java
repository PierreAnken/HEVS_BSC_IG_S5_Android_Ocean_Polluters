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

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;

;


@Dao
public abstract class ContainerDao {

    @Transaction
    @Query("SELECT * " +
            "FROM " +
            "containers s " +
            "LEFT JOIN items i ON s.e_container_id = e_container_id ")
    public abstract LiveData<List<ContainerWithItem>> getAll();


    @Transaction
    @Query("SELECT * " +
            "FROM " +
            "containers s " +
            "LEFT JOIN items i ON s.e_container_id = e_container_id " +
            "WHERE container_id = :id_container")
    public abstract LiveData<ContainerWithItem> getById(int id_container);

    @Transaction
    @Query("SELECT * " +
            "FROM " +
            "containers s " +
            "LEFT JOIN items i ON s.e_container_id = e_container_id " +
            "WHERE ship_id = :id_ship")
    public abstract LiveData<List<ContainerWithItem>> getByShipId(int id_ship);

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
