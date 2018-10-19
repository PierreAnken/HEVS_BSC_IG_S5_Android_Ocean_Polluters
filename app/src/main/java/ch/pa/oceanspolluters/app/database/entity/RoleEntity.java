package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.Role;

@Entity(tableName = "roles", primaryKeys = {"id"})
public class RoleEntity implements Role {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String name;

    public RoleEntity() {
    }

    public RoleEntity(@NonNull Role role) {
        name = role.getName();
    }

    public RoleEntity(String name) {
        this.name = name;
    }

    @Override
    public Integer getId() {
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
        if (!(obj instanceof RoleEntity)) return false;
        RoleEntity o = (RoleEntity) obj;
        return o.getId() == (this.getId());
    }
}
