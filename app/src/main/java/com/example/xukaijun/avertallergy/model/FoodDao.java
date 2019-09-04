package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface FoodDao
{
    @Query("SELECT * FROM food")
    List<Food> getAll();
    @Query("SELECT * FROM food WHERE product_name LIKE :first LIMIT 1")
    Food findByFoodName(String first);
    @Query("SELECT * FROM food WHERE code = :code LIMIT 1")
    Food findByID(int code);
    @Insert
    void insertAll(Food... foods);
    @Insert
    long insert(Food food);
    @Delete
    void delete(Food food);
    @Update(onConflict = REPLACE)
    public void updateFoods(Food... foods);
    @Query("DELETE FROM Food")
    void deleteAll();
}