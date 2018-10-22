package ch.pa.oceanspolluters.app.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executors;

import ch.pa.oceanspolluters.app.database.dao.UserDao;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

@Database(entities = {UserEntity.class, ContainerEntity.class, ItemEntity.class, PortEntity.class, ShipEntity.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    private static final String DATABASE_NAME = "oceans-polluters-databaseV1.4";

    public abstract UserDao userDao();

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
                        Executors.newSingleThreadExecutor().execute(() -> {
                            AppDatabase database = AppDatabase.getInstance(appContext);
                            initializeBaseData(database);
                        });
                    }
                }).build();
    }

    public static void initializeBaseData(final AppDatabase database) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.runInTransaction(() -> {
                database.userDao().deleteAll();

                // Generate the data for pre-population
                List<UserEntity> users = DataGenerator.generateUsers();
                database.userDao().insertAll(users);
            });
        });
    }

}
