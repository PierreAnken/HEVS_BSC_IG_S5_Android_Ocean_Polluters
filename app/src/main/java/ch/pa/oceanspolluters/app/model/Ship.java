package ch.pa.oceanspolluters.app.model;

import java.util.ArrayList;
import java.util.Date;

public interface Ship {
    Integer getId();
    int getCaptainId();
    int getDestinationPortId();
    Date getDepartureDate();

    float getMaxLoadKg();
    String getName();
}
