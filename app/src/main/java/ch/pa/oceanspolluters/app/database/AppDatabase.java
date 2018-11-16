package ch.pa.oceanspolluters.app.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import ch.pa.oceanspolluters.app.database.dao.ContainerDao;
import ch.pa.oceanspolluters.app.database.dao.ItemDao;
import ch.pa.oceanspolluters.app.database.dao.ItemTypeDao;
import ch.pa.oceanspolluters.app.database.dao.PortDao;
import ch.pa.oceanspolluters.app.database.dao.ShipDao;
import ch.pa.oceanspolluters.app.database.dao.UserDao;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

@Database(entities = {ItemTypeEntity.class, UserEntity.class, ContainerEntity.class, ItemEntity.class, PortEntity.class, ShipEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    private static final String DATABASE_NAME = "oceans-polluters-database";

    public abstract UserDao userDao();
    public abstract PortDao portDao();

    public abstract ItemTypeDao itemTypeDao();
    public abstract ItemDao itemDao();
    public abstract ContainerDao containerDao();
    public abstract ShipDao shipDao();

    //Singleton database
    public static AppDatabase getInstance(final Context appContext) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance = buildDatabase(appContext.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    //database init
    private static AppDatabase buildDatabase(final Context appContext) {

        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        AppDatabase database = AppDatabase.getInstance(appContext);
                        initializeBaseData(database);
                    }
                }).build();
    }

    public static void initializeBaseData(final AppDatabase database) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.runInTransaction(() -> {
                DataGenerator.generateBaseData(database);
            });
        });
    }

}
