package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao { @Query("SELECT * FROM user")
List<User> getAll();
    @Query("SELECT * FROM user WHERE first_name LIKE :first LIMIT 1")
    User findByFirstName(String first);
    @Query("SELECT * FROM user WHERE uid = :uId LIMIT 1")
    User findByID(int uId);
    @Insert
    void insertAll(User... users);
    @Insert
    long insert(User user);
    @Delete
    void delete(User user);
    @Update(onConflict = REPLACE)
    public void updateUsers(User... users);
    @Query("DELETE FROM user")
    void deleteAll(); }