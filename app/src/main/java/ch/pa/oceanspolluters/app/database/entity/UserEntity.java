package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.User;

@Entity(tableName = "users")
public class UserEntity extends BaseEntity implements User {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "e_user_id")
    private Integer id = null;

    @ColumnInfo(name = "user_name")
    private String name;

    @ColumnInfo(name = "password")
    private int password;

    @ColumnInfo(name = "role_id")
    private int roleId;

    public UserEntity(){

    }
    @Ignore
    public UserEntity(@NonNull User user) {
        name = user.getName();
        password = user.getPassword();
        roleId = user.getRoleId();
    }
    @Ignore
    public UserEntity( String name, int password, int roleId) {
        this.name = name;
        this.password = password;
        this.roleId = roleId;
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
    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    @Override
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof UserEntity)) return false;
        UserEntity o = (UserEntity) obj;
        return o.getId() == (this.getId());
    }

}