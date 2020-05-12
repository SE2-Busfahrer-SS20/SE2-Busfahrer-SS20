package at.aau.server.database.Table;

import java.sql.Connection;

public class User extends Table{
    private int id;
    private String mac;
    private String name;

    public User(int id, String mac, String name) {
        super();
        this.id = id;
        this.mac = mac;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
