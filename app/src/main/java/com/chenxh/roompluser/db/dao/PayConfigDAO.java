package com.chenxh.roompluser.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.chenxh.roompluser.db.entity.PayConfig;

import java.util.List;

@Dao
public interface PayConfigDAO {
    @Query("select * from PayConfig")
    List<PayConfig> getAllPayConfig();

    @Insert
    void addPayConfig(PayConfig... payConfigs);

    @Update
    void updatePayConfig(PayConfig payConfig);

    @Query("select * from PayConfig where id=:idnum")
    List<PayConfig> getPayConfigByID(int idnum);

    @Delete
    void delPayConfig(PayConfig payConfig);
}
