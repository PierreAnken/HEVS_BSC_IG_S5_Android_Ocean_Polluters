package ch.pa.oceanspolluters.app.model;

import java.util.ArrayList;

public interface Container {
    Integer getId();
    String getDockPosition();
    int getShipId();
    boolean getLoaded();
}
