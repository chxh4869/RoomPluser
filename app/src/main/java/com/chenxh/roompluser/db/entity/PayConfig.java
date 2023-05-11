package com.chenxh.roompluser.db.entity;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.chenxh.libroompluser.annotation.AlterTable;
import com.chenxh.libroompluser.annotation.AnnoAlterField;
import com.chenxh.libroompluser.annotation.AnnoModifyTable;
import com.chenxh.libroompluser.annotation.AnnoTableName;


@Entity
@AnnoTableName("PayConfig")

//@AnnoAlertTable(oldVer = 1,newVer = 2,colName = "isUse",colType = AlterTable.Type.INTEGER,action = AlterTable.Action.Mod)
//@AnnoAlertTable(oldVer = 1,newVer = 2,colName = "provinceId",colType = AlterTable.Type.INTEGER,action = AlterTable.Action.Mod)
@AnnoModifyTable(oldVer = 2,newVer = 3,sqlExuce = "CREATE TABLE IF NOT EXISTS `PayConfig` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bankType` TEXT, `provinceId` TEXT, `isUse` INTEGER NOT NULL)")
@AnnoModifyTable(oldVer = 3,newVer = 4,sqlExuce = "CREATE TABLE IF NOT EXISTS `PayConfig` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `bankType` TEXT, `isUse` INTEGER NOT NULL)")
public class PayConfig {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "bankType")
    public String bankType;

//    @ColumnInfo(name = "provinceId")
//    public String provinceId;

    @AnnoAlterField(oldVer = 1,newVer = 2,colType = AlterTable.Type.INTEGER)
    @ColumnInfo
    public boolean isUse;
//
//    @AnnoAlterField(oldVer = 1,newVer = 2,colType = AlterTable.Type.TEXT)
//    @ColumnInfo
//    public String branchNo;
//
//    @AnnoAlterField(oldVer = 2,newVer = 3,colType = AlterTable.Type.TEXT)
//    @ColumnInfo
//    public String terminalNo;
}
