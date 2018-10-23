package ch.pa.oceanspolluters.app.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.UserEntity;

/**
 * https://developer.android.com/topic/libraries/architecture/room.html#no-object-references
 */
@Dao
public abstract class UserDao {

    @Query("SELECT * FROM users WHERE id = :id")
    public abstract UserEntity getById(int id);

    @Query("SELECT * FROM users WHERE name like '%' || :name || '%' limit 1")
    public abstract UserEntity getByName(String name);

    @Query("SELECT * FROM users ORDER BY Name")
    public abstract List<UserEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(UserEntity user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<UserEntity> users);

    @Update
    public abstract void update(UserEntity user);

    @Delete
    public abstract void delete(UserEntity user);

    @Query("DELETE FROM users")
    public abstract void deleteAll();
}
