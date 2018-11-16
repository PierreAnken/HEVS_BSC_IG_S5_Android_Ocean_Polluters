package ch.pa.oceanspolluters.app.util;

import ch.pa.oceanspolluters.app.R;

public enum ItemTypes {
    Food(0, "Food"),
    Cloth(1, "Clothes"),
    Furniture(2, "Furniture"),
    Weapon(3, "Weapon"),
    Ore(4, "Ore");

    private final int id;
    private final String textEN;

    ItemTypes(int id, String textEN) {
        this.id = id;
        this.textEN = textEN;
    }

    public int id() {
        return id;
    }

    public String getTextEN() {
        return textEN;
    }

}
