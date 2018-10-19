package ch.pa.oceanspolluters.app.model;

public interface User {
    int getId();
    String getFirstname();
    String getLastname();
    String getPassword();
    Role getRole();
}
