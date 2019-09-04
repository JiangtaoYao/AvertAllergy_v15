package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Food.class}, version = 2, exportSchema = false) public abstract class FoodDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();
    private static volatile FoodDatabase INSTANCE;
    static FoodDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FoodDatabase.class) {
                if (INSTANCE == null) { INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), FoodDatabase.class, "food_database")
                                .build();
                } }
        }
        return INSTANCE; }
}