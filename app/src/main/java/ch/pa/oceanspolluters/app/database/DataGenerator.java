package ch.pa.oceanspolluters.app.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ch.pa.oceanspolluters.app.database.dao.UserDao;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.ItemTypes;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * Generates dummy data
 */
public class DataGenerator {


    public static void generateBaseData(AppDatabase db) {

        System.out.println("PAD start of random data generation");

        //init users
        db.userDao().deleteAll();
        List<UserEntity> users = new ArrayList<>();

        users.add(new UserEntity("Captain Sparrow", 1234, Roles.Captain.id()));
        users.add(new UserEntity("Docker Roth", 1234, Roles.Docker.id()));
        users.add(new UserEntity("Logistic Manager Eralde", 1234, Roles.LogisticManager.id()));
        users.add(new UserEntity("Administrator Frank", 1234, Roles.Administrator.id()));

        db.userDao().insertAll(users);


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

        calendar.add(Calendar.HOUR, 6);
        ships.add(new ShipEntity("Manila Maersk",  20568, captain.getId(), portsWithId.get(0).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 125);
        ships.add(new ShipEntity("Ever Given",  20338, captain.getId(), portsWithId.get(1).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 132);
        ships.add(new ShipEntity("MSC Mirjam",  19462, captain.getId(), portsWithId.get(2).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 224);
        ships.add(new ShipEntity("Al Nefud",  18800, captain.getId(), portsWithId.get(3).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 131);
        ships.add(new ShipEntity("MSC Diana",  19462, captain.getId(), portsWithId.get(4).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 186);
        ships.add(new ShipEntity("YM Wellness",  14080, captain.getId(), portsWithId.get(5).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 236);
        ships.add(new ShipEntity("Tihama",  18800, captain.getId(), portsWithId.get(2).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 203);
        ships.add(new ShipEntity("CMA CGM Zheng He",  17859, captain.getId(), portsWithId.get(2).getId(), calendar.getTime()));

        db.shipDao().insertAll(ships);
        List<ShipEntity> shipsWithId = db.shipDao().getAll();

        //init containers
        db.containerDao().deleteAll();
        List<ContainerEntity> containers = new ArrayList<>();

        //we add 200 containers randomly to ships
        for(int i = 0; i<200; i++){

            int shipId = (int)Math.floor(Math.random()*shipsWithId.size()+1);
            boolean loaded = Math.random()>0.3;

            char[] alphabetNumber = "abcdefghijklmnopqrstuvwxyz01234567890".toUpperCase().toCharArray();
            String dockPosition = "";
            for(int j = 0; j<8; j++)
                dockPosition+= alphabetNumber[(int)Math.floor(Math.random()*alphabetNumber.length)];

            containers.add(new ContainerEntity(dockPosition,shipId,loaded));

        }
        db.containerDao().insertAll(containers);
        List<ContainerEntity> containersWithId = db.containerDao().getAll();

        //init items
        db.itemDao().deleteAll();
        List<ItemEntity> items = new ArrayList<>();


        //for each container
        for(int i = 0; i<containersWithId.size(); i++){

            //we add 5 - 50 random items to container
            int numberItem = (int)Math.floor(Math.random()*45+5);
            for(int j = 0; j<numberItem; j++){

                ItemTypes[] typesId = ItemTypes.values();
                int type = typesId[(int)Math.floor(Math.random()*typesId.length)].id();

                float weight = (float)Math.random()*50;

                ItemEntity item = new ItemEntity(type, weight, (int)containersWithId.get(i).getId());
            }
        }

        db.itemDao().insertAll(items);
        System.out.println("PAD end of random data generation");
    }

}
