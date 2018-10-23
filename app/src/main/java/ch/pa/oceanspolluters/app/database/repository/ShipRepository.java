package ch.pa.oceanspolluters.app.database.repository;

import java.util.List;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;

public class ShipRepository {
    private static ShipRepository sInstance;

    private final AppDatabase mDatabase;

    private ShipRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static ShipRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (ShipRepository.class) {
                if (sInstance == null) {
                    sInstance = new ShipRepository(database);
                }
            }
        }
        return sInstance;
    }

    public ShipEntity getShip(final int id) {
        return mDatabase.shipDao().getById(id);
    }

    public List<ShipEntity> getShips() {
        return mDatabase.shipDao().getAll();
    }

    public void insert(final ShipEntity ship) {
        mDatabase.shipDao().insert(ship);
    }

    public void update(final ShipEntity ship) {
        mDatabase.shipDao().update(ship);
    }

    public void delete(final ShipEntity ship) {
        mDatabase.shipDao().delete(ship);
    }

}
