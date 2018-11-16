package ch.pa.oceanspolluters.app.util;

public enum Roles {
    Captain(0),
    Docker(1),
    LogisticManager(2);

    private final int id;

    Roles(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
