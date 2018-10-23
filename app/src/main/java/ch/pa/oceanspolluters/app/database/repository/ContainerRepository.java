package ch.pa.oceanspolluters.app.database.repository;

import java.util.List;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;

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

    public ContainerEntity getContainer(final int id) {
        return mDatabase.containerDao().getById(id);
    }

    public List<ContainerEntity> getContainers() {
        return mDatabase.containerDao().getAll();
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
