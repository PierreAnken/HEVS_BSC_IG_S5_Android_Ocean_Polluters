package ch.pa.oceanspolluters.app.database.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;

public class PortRepository {
    private static PortRepository sInstance;

    private final AppDatabase mDatabase;

    private PortRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static PortRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (PortRepository.class) {
                if (sInstance == null) {
                    sInstance = new PortRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<PortEntity> getPort(final int id) {
        return mDatabase.portDao().getById(id);
    }

    public LiveData<List<PortEntity>> getPorts() {
        return mDatabase.portDao().getAll();
    }

    public void insert(final PortEntity port) {
        mDatabase.portDao().insert(port);
    }

    public void update(final PortEntity port) {
        mDatabase.portDao().update(port);
    }

    public void delete(final PortEntity port) {
        mDatabase.portDao().delete(port);
    }



}
