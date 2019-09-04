package com.example.xukaijun.avertallergy.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Allergy.class}, version = 2, exportSchema = false) public abstract class AllergyDatabase extends RoomDatabase {
    public abstract AllergyDao allergyDao();
    private static volatile AllergyDatabase INSTANCE;
    static AllergyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AllergyDatabase.class) {
                if (INSTANCE == null) { INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), AllergyDatabase.class, "allergy_database")
                                .build();
                } }
        }
        return INSTANCE; }
}