package ch.pa.oceanspolluters.app.model;

public interface User {
    Integer getId();
    String getFirstname();
    String getLastname();

    int getPassword();

    int getRoleId();
}
