package com.chenxh.roompluser.db.entity;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.chenxh.libroompluser.annotation.AlterTable;
import com.chenxh.libroompluser.annotation.AnnoAlertTable;
import com.chenxh.libroompluser.annotation.AnnoAlterField;
import com.chenxh.libroompluser.annotation.AnnoModifyTable;
import com.chenxh.libroompluser.annotation.AnnoTableName;


@Entity
@AnnoTableName("PayConfig")

//如果有改表需要配上改动内容，否则会抛异常
@AnnoAlertTable(oldVer = 3,newVer = 4,colName = "provinceId",colType = AlterTable.Type.TEXT,action = AlterTable.Action.Del)
@AnnoAlertTable(oldVer = 2,newVer = 3,colName = "provinceId",colType = AlterTable.Type.TEXT,action = AlterTable.Action.Mod)

//sqlExcue语句不用自己写，可以在自己实现的的RoomDatabase子类由编译自动生成的_Impl.java中获取,目录build/generated/ap_generated_sources/debug/out/packagnmae下面找到
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
