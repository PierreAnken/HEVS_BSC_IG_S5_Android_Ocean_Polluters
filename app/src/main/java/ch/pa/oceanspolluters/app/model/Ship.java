package ch.pa.oceanspolluters.app.model;

import java.util.ArrayList;
import java.util.Date;

public interface Ship {
    int getId();
    User getCaptain();
    Port getPort();
    Date getDepartureDate();
    int getMaxLoad();
    ArrayList<Container> getContainerList();
}
