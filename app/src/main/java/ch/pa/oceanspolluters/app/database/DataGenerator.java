package ch.pa.oceanspolluters.app.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemEntity;
import ch.pa.oceanspolluters.app.database.entity.ItemTypeEntity;
import ch.pa.oceanspolluters.app.database.entity.PortEntity;
import ch.pa.oceanspolluters.app.database.entity.ShipEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * Generates dummy data
 */
public class DataGenerator {

    private static final String TAG = "DataGenerator";
    private static  DatabaseReference fireBaseDB = FirebaseDatabase.getInstance().getReference();
    //users - ports - ships - container

    public static void initFireBaseData(){
        //we init all data in Cascade du to asynchrone mode of FireBase
        initUsers();
    }

    private static void initShips(){
        //check/init ships
        fireBaseDB.child("ships").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() != 8){
                    fireBaseDB.child("ships").removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : error init users");
            }
        });
    }

    private static void initUsers(){
        //check/init users
        fireBaseDB.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() != 3){
                    fireBaseDB.child("users").removeValue();
                    List<UserEntity> users = new ArrayList<>();

                    users.add(new UserEntity("Captain Sparrow", 1234, Roles.Captain.id()));
                    users.add(new UserEntity("Docker Roth", 1234, Roles.Docker.id()));
                    users.add(new UserEntity("Logistic Manager Eralde", 1234, Roles.LogisticManager.id()));

                    for (UserEntity user:users) {
                        fireBaseDB.child("users").push().setValue(user);
                    }
                    initPorts();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : error init users");
            }
        });
    }

    private static void initPorts(){
        //check/init ports
        fireBaseDB.child("ports").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() != 5){
                    fireBaseDB.child("ports").removeValue();
                    List<PortEntity> ports = new ArrayList<>();

                    ports.add(new PortEntity("Rotterdam"));
                    ports.add(new PortEntity("Anvers"));
                    ports.add(new PortEntity("Hambourg"));
                    ports.add(new PortEntity("Algésiras"));
                    ports.add(new PortEntity("Marseille"));

                    for (PortEntity port:ports) {
                        fireBaseDB.child("ports").push().setValue(port);
                        System.out.println("PA_DEBUG : error init ports");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static void generateBaseData(AppDatabase db) {

        Log.d(TAG, "PA_Debug start of random data generation");

        //init users
        db.userDao().deleteAll();
        List<UserEntity> users = new ArrayList<>();

        users.add(new UserEntity("Captain Sparrow", 1234, Roles.Captain.id()));
        users.add(new UserEntity("Docker Roth", 1234, Roles.Docker.id()));
        users.add(new UserEntity("Logistic Manager Eralde", 1234, Roles.LogisticManager.id()));

        //insert into local db
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
        ships.add(new ShipEntity("YM Wellness",  14080, captain.getId(), portsWithId.get(3).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 236);
        ships.add(new ShipEntity("Tihama",  18800, captain.getId(), portsWithId.get(0).getId(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 203);
        ships.add(new ShipEntity("CMA CGM Zheng He",  17859, captain.getId(), portsWithId.get(1).getId(), calendar.getTime()));

        db.shipDao().insertAll(ships);
        List<ShipWithContainer> shipsWithId = db.shipDao().getAll();

        //init containers
        db.containerDao().deleteAll();
        List<ContainerEntity> containers = new ArrayList<>();

        //we add 30 containers randomly to ships
        for(int i = 0; i<100; i++){

            int shipId = (int)Math.floor(Math.random()*shipsWithId.size()+1);
            boolean loaded = Math.random()>0.3;

            char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase().toCharArray();
            char[] numbers = "0123456789".toCharArray();

            StringBuilder dockPosition = new StringBuilder();
            StringBuilder containerName = new StringBuilder();

            for(int j = 0; j<8; j++){
                containerName.append(alphabet[(int)Math.floor(Math.random()*alphabet.length)]);

                if(j == 2)
                    dockPosition.append(alphabet[(int)Math.floor(Math.random()*alphabet.length)]);
                else if(j < 4){
                    dockPosition.append(numbers[(int)Math.floor(Math.random()*numbers.length)]);
                }

            }
            containers.add(new ContainerEntity(containerName.toString(),dockPosition.toString(),shipId,loaded));

        }
        db.containerDao().insertAll(containers);
        List<ContainerWithItem> containersWithId = db.containerDao().getAll();

        //init itemTypes
        db.itemTypeDao().deleteAll();
        List<ItemTypeEntity> itemTypes = new ArrayList<>();

        itemTypes.add(new ItemTypeEntity("Food"));
        itemTypes.add(new ItemTypeEntity("Clothes"));
        itemTypes.add(new ItemTypeEntity("Furnitures"));
        itemTypes.add(new ItemTypeEntity("Weapons"));
        itemTypes.add(new ItemTypeEntity("Ore"));
        db.itemTypeDao().insertAll(itemTypes);

        List<ItemTypeEntity> itemTypesWithId = db.itemTypeDao().getAll();

        //init items
        db.itemDao().deleteAll();
        List<ItemEntity> items = new ArrayList<>();

        //for each container
        for(int i = 0; i<containersWithId.size(); i++){

            //we add 3 - 10 random items to container
            int numberItem = (int)Math.floor(Math.random()*7+3);

            for(int j = 0; j<numberItem; j++){

                int itemTypeId = itemTypesWithId.get((int) Math.floor(Math.random() * itemTypesWithId.size())).getId();
                float weight = (float)Math.random()*50;

                ItemEntity item = new ItemEntity(itemTypeId, weight, containersWithId.get(i).container.getId());

                items.add(item);
            }
        }

       db.itemDao().insertAll(items);


        Log.d(TAG, "PA_Debug end of random data generation");
    }

}
