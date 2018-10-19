package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.Item;
import ch.pa.oceanspolluters.app.model.Port;

@Entity(tableName = "ports", primaryKeys = {"id"})
public class PortEntity implements Port {

    @NonNull
    private int id;
    private String name;

    public PortEntity() {
    }

    public PortEntity(@NonNull Port port) {
        name = port.getName();
    }

    public PortEntity(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof PortEntity)) return false;
        PortEntity o = (PortEntity) obj;
        return o.getId() == (this.getId());
    }
}
