package ch.pa.oceanspolluters.app.model;

import ch.pa.oceanspolluters.app.util.ItemTypes;

public interface Item {
    Integer getId();
    ItemTypes getItemType();
    float getWeightKg();
    Integer getContainerId();
}
