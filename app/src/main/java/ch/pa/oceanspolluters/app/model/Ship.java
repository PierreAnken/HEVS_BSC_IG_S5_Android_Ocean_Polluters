package ch.pa.oceanspolluters.app.model;

import java.util.ArrayList;
import java.util.Date;

public interface Ship {
    int getId();
    int getCaptainId();
    int getDestinationPortId();
    Date getDepartureDate();
    int getMaxLoad();
    ArrayList<Container> getContainerList();
    String getName();
}
