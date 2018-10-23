package ch.pa.oceanspolluters.app.database;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * Generates dummy data
 */
public class DataGenerator {

    public static void generateBaseData(AppDatabase database) {


        //init users
        database.userDao().deleteAll();

        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity("Captain Sparrow", 1234, Roles.Captain.id()));
        users.add(new UserEntity("Docker Roth", 1234, Roles.Docker.id()));
        users.add(new UserEntity("Logistic Manager Eralde", 1234, Roles.LogisticManager.id()));
        users.add(new UserEntity("Administrator Frank", 1234, Roles.Administrator.id()));
        database.userDao().insertAll(users);

        List<UserEntity> usersWithId = database.userDao().getAll();


        //init ports
        database.portDao().deleteAll();

        List<PortEntity> ports = new ArrayList<>();
        ports.add(new PortEntity("Rotterdam"));
        ports.add(new PortEntity("Anvers"));
        ports.add(new PortEntity("Hambourg"));
        ports.add(new PortEntity("Algésiras"));
        ports.add(new PortEntity("Marseille"));
        database.portDao().insertAll(ports);

        List<PortEntity> portsWithId = database.portDao().getAll();


    }

}
