package ch.pa.oceanspolluters.app.util;

public enum ItemTypes {
    Food(0),
    Cloth(1),
    Furniture(2),
    Weapon(3),
    Ore(4);

    private final int id;

    ItemTypes(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
