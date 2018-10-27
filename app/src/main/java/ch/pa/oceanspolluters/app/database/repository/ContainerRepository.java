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

    public LiveData<ContainerWithItem> getContainer(final int id) {
        return mDatabase.containerDao().getById(id);
    }

    public LiveData<List<ContainerWithItem>> getContainers() {
        return mDatabase.containerDao().getAll();
    }

    public LiveData<List<ContainerWithItem>> getByShipId(final int shipId) {
        return mDatabase.containerDao().getByShipId(shipId);
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
