package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Allergy {

    @PrimaryKey public int aid;
    @ColumnInfo(name = "allergy_name")public String allergyname;



    public int getId() {
        return aid;
    }

    public void setId(int id) {
        this.aid = id;
    }

    public String getAllergyName() {
        return allergyname;
    }

    public void setAllergyName(String name) {
        this.allergyname = name;
    }
}