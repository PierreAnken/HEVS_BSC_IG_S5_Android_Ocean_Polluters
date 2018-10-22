package ch.pa.oceanspolluters.app.database;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.database.entity.RoleEntity;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;

/**
 * Generates dummy data
 */
public class DataGenerator {

    public static List<RoleEntity> generateRoles() {

        List<RoleEntity> roles = new ArrayList<>();

        roles.add(new RoleEntity("Captain",0));
        roles.add(new RoleEntity("Docker",1));
        roles.add(new RoleEntity("Logistics Manager",2));

        return roles;
    }

    public static List<UserEntity> generateUsers() {

        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity(0,"John", "Smith", 12345, 0));
        users.add(new UserEntity(1,"Lewis", "Roth", 12345, 1));
        users.add(new UserEntity(2,"Wilson", "Snord", 12345, 2));
        return users;
    }

}
