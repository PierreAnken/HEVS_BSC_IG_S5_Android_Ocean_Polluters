package ch.pa.oceanspolluters.app.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.database.dao.UserDao;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * Generates dummy data
 */
public class DataGenerator {

    public static void generateBaseData(AppDatabase db) {


        //init users
        db.userDao().deleteAll();
        List<UserEntity> users = new ArrayList<>();

        users.add(new UserEntity("Captain Sparrow", 1234, Roles.Captain.id()));
        users.add(new UserEntity("Docker Roth", 1234, Roles.Docker.id()));
        users.add(new UserEntity("Logistic Manager Eralde", 1234, Roles.LogisticManager.id()));
        users.add(new UserEntity("Administrator Frank", 1234, Roles.Administrator.id()));

        db.userDao().insertAll(users);
        List<UserEntity> usersWithId = db.userDao().getAll();


        //init ports
        db.portDao().deleteAll();
        List<PortEntity> ports = new ArrayList<>();

        ports.add(new PortEntity("Rotterdam"));
        ports.add(new PortEntity("Anvers"));
        ports.add(new PortEntity("Hambourg"));
        ports.add(new PortEntity("Algésiras"));
        ports.add(new PortEntity("Marseille"));

        db.portDao().insertAll(ports);
        List<PortEntity> portsWithId = db.portDao().getAll();


        //init ships
        db.shipDao().deleteAll();
        List<ShipEntity> ships = new ArrayList<>();

        UserEntity captain = db.userDao().getByName("Captain");
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, 2);
        ships.add(new ShipEntity("Manila Maersk",  20568, captain.getId(), portsWithId.get(0).getId(), calendar.getTime().toString()));
        calendar.add(Calendar.DATE, 5);
        ships.add(new ShipEntity("Ever Given",  20338, captain.getId(), portsWithId.get(1).getId(), calendar.getTime().toString()));
        calendar.add(Calendar.DATE, 6);
        ships.add(new ShipEntity("MSC Mirjam",  19462, captain.getId(), portsWithId.get(2).getId(), calendar.getTime().toString()));
        calendar.add(Calendar.DATE, 23);
        ships.add(new ShipEntity("Al Nefud",  18800, captain.getId(), portsWithId.get(3).getId(), calendar.getTime().toString()));
        calendar.add(Calendar.DATE, 14);
        ships.add(new ShipEntity("YM Wellness",  14080, captain.getId(), portsWithId.get(4).getId(), calendar.getTime().toString()));

        db.shipDao().insertAll(ships);


    }

}
