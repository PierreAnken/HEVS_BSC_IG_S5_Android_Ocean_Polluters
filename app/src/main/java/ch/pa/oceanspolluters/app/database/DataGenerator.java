package ch.pa.oceanspolluters.app.database;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * Generates dummy data
 */
public class DataGenerator {

    private static final String TAG = "DataGenerator";
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();
    private static List<UserEntity> users;
    private static List<PortEntity> ports;
    private static List<ShipEntity> ships;
    private static List<ItemTypeEntity> itemTypes;
    private static List<ContainerEntity> containers;
    private static List<ItemEntity> items;

    //users - ports - ships - container


    public static void initFireBaseData(){
        fireBaseDB.getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getChildrenCount() != 3){
                    //base items
                    fireBaseDB.getReference().removeValue();
                    initItemTypesFB();
                    initUsersFB();
                    initPortsFB();

                    //pojo
                    initShipsFB();
                    initContainersFB();
                    initItemsFB();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : error init users");
            }
        });
    }


    private static void initItemTypesFB(){

        itemTypes = new ArrayList<>();
        itemTypes.add(new ItemTypeEntity("Food"));
        itemTypes.add(new ItemTypeEntity("Clothes"));
        itemTypes.add(new ItemTypeEntity("Furnitures"));
        itemTypes.add(new ItemTypeEntity("Weapons"));
        itemTypes.add(new ItemTypeEntity("Ore"));

        for (ItemTypeEntity itemType:itemTypes) {
            itemType.setFB_Key(fireBaseDB.getReference().child("itemTypes").push().getKey());
            fireBaseDB.getReference("itemTypes/"+itemType.getFB_Key()).setValue(itemType);
        }
    }

    private static void initUsersFB(){
        //check/init users
        users = new ArrayList<>();

        users.add(new UserEntity("Captain Sparrow", 1234, Roles.Captain.id()));
        users.add(new UserEntity("Docker Roth", 1234, Roles.Docker.id()));
        users.add(new UserEntity("Logistic Manager Eralde", 1234, Roles.LogisticManager.id()));

        for (UserEntity user:users) {
            user.setFB_Key( fireBaseDB.getReference().child("users").push().getKey());
            fireBaseDB.getReference().child("users").child(user.getFB_Key()).setValue(user);
        }
    }

    private static void initPortsFB(){

        ports = new ArrayList<>();

        ports.add(new PortEntity("Rotterdam"));
        ports.add(new PortEntity("Anvers"));
        ports.add(new PortEntity("Hambourg"));
        ports.add(new PortEntity("Algésiras"));
        ports.add(new PortEntity("Marseille"));

        for (PortEntity port:ports) {
            port.setFB_Key(fireBaseDB.getReference().child("ports").push().getKey());
            fireBaseDB.getReference().child("ports").child(port.getFB_Key()).setValue(port);
        }
    }

    private static void initShipsFB(){

        ships = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.HOUR, 6);
        ships.add(new ShipEntity("Manila Maersk",  20568, users.get(0).getFB_Key(), ports.get(0).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 125);
        ships.add(new ShipEntity("Ever Given",  20338, users.get(0).getFB_Key(), ports.get(1).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 132);
        ships.add(new ShipEntity("MSC Mirjam",  19462, users.get(0).getFB_Key(), ports.get(2).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 224);
        ships.add(new ShipEntity("Al Nefud",  18800, users.get(0).getFB_Key(), ports.get(3).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 131);
        ships.add(new ShipEntity("MSC Diana",  19462, users.get(0).getFB_Key(), ports.get(4).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 186);
        ships.add(new ShipEntity("YM Wellness",  14080, users.get(0).getFB_Key(), ports.get(3).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 236);
        ships.add(new ShipEntity("Tihama",  18800, users.get(0).getFB_Key(), ports.get(0).getFB_Key(), calendar.getTime()));

        calendar.add(Calendar.HOUR, 203);
        ships.add(new ShipEntity("CMA CGM Zheng He",  17859, users.get(0).getFB_Key(), ports.get(1).getFB_Key(), calendar.getTime()));

        for (ShipEntity ship:ships) {

            //set the ship
            ship.setFB_Key(fireBaseDB.getReference().child("ships").push().getKey());
            fireBaseDB.getReference().child("ships").child(ship.getFB_Key()).setValue(ship);

            //add the captain
            fireBaseDB.getReference("ships/"+ship.getFB_Key()+"/captain").setValue(users.get(0));

            //add the port
            for(PortEntity port : ports){
                if(port.getFB_Key().equals(ship.getFB_destinationPortId())){
                fireBaseDB.getReference("ships/"+ship.getFB_Key()+"/port").setValue(port);
                break;
                }
            }
        }
    }

    private static void initContainersFB(){

        containers = new ArrayList<>();

        for(int i = 0; i<100; i++){

            String shipIdFB = ships.get((int)Math.floor(Math.random()*ships.size())).getFB_Key();
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
            containers.add(new ContainerEntity(containerName.toString(),dockPosition.toString(),shipIdFB,loaded));

        }

        for (ContainerEntity container:containers) {

            container.setFB_Key(fireBaseDB.getReference("ships/"+container.getFB_shipId()+"/containers").push().getKey());
            fireBaseDB.getReference("ships/"+container.getFB_shipId()+"/containers/"+container.getFB_Key()).setValue(container);
        }
    }


    private static void initItemsFB(){

        items = new ArrayList<>();

        //for each container
        for(int i = 0; i<containers.size(); i++){

            //we add 3 - 10 random items to container
            int numberItem = (int)Math.floor(Math.random()*7+3);

            for(int j = 0; j<numberItem; j++){

                int idArrayType = (int) Math.floor(Math.random() * itemTypes.size());
                String itemTypeIdFB = itemTypes.get(idArrayType).getFB_Key();
                float weight = (float)Math.random()*50;

                ItemEntity item = new ItemEntity(itemTypeIdFB, weight, containers.get(i).getFB_Key());

                item.setFB_Key(fireBaseDB.getReference("ships/"+containers.get(i).getFB_shipId()+"/containers/"+containers.get(i).getFB_Key()+"/items").push().getKey());
                fireBaseDB.getReference("ships/"+containers.get(i).getFB_shipId()+"/containers/"+containers.get(i).getFB_Key()+"/items/"+item.getFB_Key()).setValue(item);

                //set itemType
                fireBaseDB.getReference("ships/"+containers.get(i).getFB_shipId()+"/containers/"+containers.get(i).getFB_Key()+"/items/"+item.getFB_Key()+"/itemType").setValue(itemTypes.get(idArrayType));

                items.add(item);
            }
        }
    }
}
