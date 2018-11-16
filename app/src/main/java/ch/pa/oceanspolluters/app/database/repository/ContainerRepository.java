package ch.pa.oceanspolluters.app.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;

public class ContainerRepository {
    private static ContainerRepository sInstance;

    private final AppDatabase mDatabase;

    private ContainerRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static ContainerRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (ContainerRepository.class) {
                if (sInstance == null) {
                    sInstance = new ContainerRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<ContainerWithItem> getContainerLD(final int id) {
        return mDatabase.containerDao().getByIdLD(id);
    }

    public LiveData<List<ContainerWithItem>> getContainersLD() {
        return mDatabase.containerDao().getAllLD();
    }

    public LiveData<List<ContainerWithItem>> getContainerToLoadLD() {
        return mDatabase.containerDao().getToLoadLD();
    }

    public LiveData<List<ContainerWithItem>>  getByShipIdLD(final int shipId) {
        return mDatabase.containerDao().getByShipIdLD(shipId);
    }

    public LiveData<List<ContainerWithItem>> getByShipIdLDToLoad(final int shipId) {
        return mDatabase.containerDao().getByShipIdLDToLoad(shipId);
    }

    public void insert(final ContainerEntity container) {
        mDatabase.containerDao().insert(container);
    }

    public void update(final ContainerEntity container) {
        mDatabase.containerDao().update(container);
    }

    public void delete(final ContainerEntity container) {
        mDatabase.containerDao().delete(container);
    }



}
