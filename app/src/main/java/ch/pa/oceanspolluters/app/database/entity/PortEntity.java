package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.Port;

@Entity(tableName = "ports")
public class PortEntity implements Port {

    @PrimaryKey(autoGenerate = true)
    private Integer id;
    private String name;

    public PortEntity() {
    }

    @Ignore
    public PortEntity(@NonNull Port port) {
        name = port.getName();
    }

    @Ignore
    public PortEntity(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){ this.id = id;}

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
