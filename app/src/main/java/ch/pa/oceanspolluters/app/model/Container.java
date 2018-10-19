package ch.pa.oceanspolluters.app.model;

import java.util.ArrayList;

public interface Container {
    Integer getId();
    ArrayList<Item> getItemList();
    String getDockPosition();
    int getShipId();
    boolean getLoadingStatus();
}
