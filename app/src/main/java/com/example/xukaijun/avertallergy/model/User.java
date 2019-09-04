package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey public int uid;
    @ColumnInfo(name = "first_name")public String firstname;



    public int getId() {
        return uid;
    }

    public void setId(int id) {
        this.uid = id;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String name) {
        this.firstname = name;
    }
}