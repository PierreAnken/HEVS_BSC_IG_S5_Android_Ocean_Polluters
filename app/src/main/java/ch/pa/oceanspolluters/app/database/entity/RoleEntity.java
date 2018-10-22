package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.Role;

@Entity(tableName = "roles")
public class RoleEntity implements Role {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;

    @Ignore
    public RoleEntity(@NonNull Role role) {
        name = role.getName();
        id = role.getId();
    }

    public RoleEntity(String name, int id) {
        this.name = name;
        this.id = id;
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
        if (!(obj instanceof RoleEntity)) return false;
        RoleEntity o = (RoleEntity) obj;
        return o.getId() == (this.getId());
    }
}
