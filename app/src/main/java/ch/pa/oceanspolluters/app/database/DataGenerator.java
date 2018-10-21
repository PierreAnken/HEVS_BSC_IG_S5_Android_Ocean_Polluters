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

        roles.add(new RoleEntity("Captain"));
        roles.add(new RoleEntity("Docker"));
        roles.add(new RoleEntity("Logistics Manager"));

        return roles;
    }

    public static List<UserEntity> generateUsers() {

        List<UserEntity> users = new ArrayList<>();
        users.add(new UserEntity("John", "Smith", 12345, 0));
        users.add(new UserEntity("Lewis", "Roth", 12345, 1));
        users.add(new UserEntity("Wilson", "Snord", 12345, 2));
        return users;
    }

}
