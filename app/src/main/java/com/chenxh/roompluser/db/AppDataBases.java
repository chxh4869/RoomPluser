package com.chenxh.roompluser.db;

import android.os.Environment;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.chenxh.libroompluser.MigrationCreater;
import com.chenxh.roompluser.StartApplication;
import com.chenxh.roompluser.db.dao.PayConfigDAO;
import com.chenxh.roompluser.db.entity.PayConfig;

import java.io.File;

@Database(entities = {PayConfig.class},version = 4,exportSchema = false)
public abstract class AppDataBases extends RoomDatabase {
    public abstract PayConfigDAO payConfigDao();
    private static AppDataBases db = null;
    public static AppDataBases getInstance(){
        if(db==null){
            synchronized (AppDataBases.class){
                if(db == null){
                    db = DBCreater.create();
                }
            }
        }
        return db;
    }

    private static class DBCreater{

        final static String dbName = Environment.getExternalStorageDirectory().getPath()+ File.separator + "test.db";

        public static AppDataBases create(){
            RoomDatabase db = null;
            try {
                db = Room.databaseBuilder(StartApplication.getContext(), AppDataBases.class,dbName)
                       .allowMainThreadQueries()
   //                    .addMigrations(new Migration(1, 2) {
   //                        @Override
   //                        public void migrate(@NonNull SupportSQLiteDatabase database) {
   //                            database.execSQL("ALTER TABLE PayConfig add CO");
   //                        }
   //                    })
                       .addMigrations(new MigrationCreater().create(4,PayConfig.class))
                       .build();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return (AppDataBases) db;
        }

    }

}
