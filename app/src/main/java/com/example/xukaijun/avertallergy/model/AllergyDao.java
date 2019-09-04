package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface AllergyDao
{
    @Query("SELECT * FROM allergy")
    List<Allergy> getAll();
    @Query("SELECT * FROM allergy WHERE allergy_name LIKE :first LIMIT 1")
    Allergy findByAllergyName(String first);
    @Query("SELECT * FROM allergy WHERE aid = :aId LIMIT 1")
    Allergy findByID(int aId);
    @Insert
    void insertAll(Allergy... allergys);
    @Insert
    long insert(Allergy allergy);
    @Delete
    void delete(Allergy allergy);
    @Update(onConflict = REPLACE)
    public void updateAllergys(Allergy... allergys);
    @Query("DELETE FROM Allergy")
    void deleteAll();
}