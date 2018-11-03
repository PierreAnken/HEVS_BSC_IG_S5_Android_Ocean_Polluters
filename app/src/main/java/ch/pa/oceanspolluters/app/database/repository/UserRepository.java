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

    public LiveData<UserEntity> getUser(final int id) {
        return mDatabase.userDao().getById(id);
    }

    public LiveData<UserEntity> getByName(String name) {
        return mDatabase.userDao().getByName(name);
    };

    public LiveData<List<UserEntity>> getUsers() {
        return mDatabase.userDao().getAll();
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
