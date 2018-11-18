package ch.pa.oceanspolluters.app.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

public class UserRepository {
    private static UserRepository sInstance;

    private final AppDatabase mDatabase;

    private UserRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static UserRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<UserEntity> getUserLD(final int id) {
        return mDatabase.userDao().getByIdLD(id);
    }

    public LiveData<UserEntity> getByNameLD(String name) {
        return mDatabase.userDao().getByNameLD(name);
    }

    public LiveData<List<UserEntity>> getUsersLD() {
        return mDatabase.userDao().getAllLD();
    }

    public void insert(final UserEntity user) {
        mDatabase.userDao().insert(user);
    }

    public void update(final UserEntity user) {
        mDatabase.userDao().update(user);
    }

    public void delete(final UserEntity user) {
        mDatabase.userDao().delete(user);
    }




}
