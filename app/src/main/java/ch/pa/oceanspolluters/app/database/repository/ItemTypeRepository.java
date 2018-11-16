package ch.pa.oceanspolluters.app.database.repository;

import java.util.List;

import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;

public class ItemTypeRepository {
    private static ItemTypeRepository sInstance;

    private final AppDatabase mDatabase;

    private ItemTypeRepository(final AppDatabase database) {
        mDatabase = database;
    }

    public static ItemTypeRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (ItemTypeRepository.class) {
                if (sInstance == null) {
                    sInstance = new ItemTypeRepository(database);
                }
            }
        }
        return sInstance;
    }

    public List<ItemTypeEntity> getAll() {
        return mDatabase.itemTypeDao().getAll();
    }

    public void insert(final ItemTypeEntity itemTypeEntity) {
        mDatabase.itemTypeDao().insert(itemTypeEntity);
    }

    public void update(final ItemTypeEntity itemTypeEntity) {
        mDatabase.itemTypeDao().update(itemTypeEntity);
    }

    public void delete(final ItemTypeEntity itemTypeEntity) {
        mDatabase.itemTypeDao().delete(itemTypeEntity);
    }


}
