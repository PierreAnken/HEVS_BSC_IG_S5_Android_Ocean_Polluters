package ch.pa.oceanspolluters.app.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import ch.pa.oceanspolluters.app.model.User;

@Entity(tableName = "users")
public class UserEntity implements User {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    private String firstname;
    private String lastname;
    private int password;

    @ColumnInfo(name = "role_id")
    private int roleId;

    public UserEntity(){

    }
    @Ignore
    public UserEntity(@NonNull User user) {
        id = user.getId();
        firstname = user.getFirstname();
        lastname = user.getLastname();
        password = user.getPassword();
        roleId = user.getRoleId();
    }
    @Ignore
    public UserEntity(int id, String firstname, String lastname, int password, int roleId) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.roleId = roleId;
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }
    public void setId(Integer id){ this.id = id;}

    @Override
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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