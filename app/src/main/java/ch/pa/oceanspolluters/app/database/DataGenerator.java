package ch.pa.oceanspolluters.app.database;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * Generates dummy data
 */
public class DataGenerator {

    public static List<UserEntity> generateUsers() {

        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity(0,"Captain", "Sparrow", 1234, Roles.Captain.id()));
        users.add(new UserEntity(1,"Docker", "Roth", 1234, Roles.Docker.id()));
        users.add(new UserEntity(2,"Logistic Manager", "Eralde", 1234, Roles.LogisticManager.id()));
        users.add(new UserEntity(3,"Administrator", "Frank", 1234, Roles.Administrator.id()));
        return users;
    }

}
