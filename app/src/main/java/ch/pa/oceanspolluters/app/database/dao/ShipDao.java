package ch.pa.oceanspolluters.app.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;

;


@Dao
public abstract class ShipDao {

    @Query("SELECT * FROM ships WHERE e_ship_id = :id")
    public abstract ShipEntity getById(int id);

    @Query("SELECT * FROM ships ORDER BY ship_name")
    public abstract List<ShipEntity> getAll();

    @Transaction
    @Query("SELECT * " +
            "FROM " +
                "ships s " +
                "INNER JOIN users u ON s.captain_id = e_user_id " +
                "INNER JOIN ports p ON s.destination_port_id = p.e_port_id " +
            "WHERE captain_id = :id_captain")
    public abstract List<ShipWithContainer> getShipsFromCaptain(int id_captain);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(ShipEntity ship);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<ShipEntity> ships);

    @Update
    public abstract void update(ShipEntity ship);

    @Delete
    public abstract void delete(ShipEntity ship);

    @Query("DELETE FROM ships")
    public abstract void deleteAll();
}
