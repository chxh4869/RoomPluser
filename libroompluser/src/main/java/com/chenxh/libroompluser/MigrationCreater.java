package com.chenxh.libroompluser;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.chenxh.libroompluser.annotation.AlterTable;
import com.chenxh.libroompluser.annotation.AnnoAlertTable;
import com.chenxh.libroompluser.annotation.AnnoAlertTables;
import com.chenxh.libroompluser.annotation.AnnoAlterField;
import com.chenxh.libroompluser.annotation.AnnoIgnoe;
import com.chenxh.libroompluser.annotation.AnnoModifyTable;
import com.chenxh.libroompluser.annotation.AnnoModifyTables;
import com.chenxh.libroompluser.annotation.AnnoTableName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MigrationCreater {
    ArrayList<Migration> list = new ArrayList<Migration>();
    Map<Integer, List<String>> sqlmap;
    Map<Integer, List<String>> modifiesMap;

    public Migration[] create(int newVer, Class... cls) throws Exception {

        Migration[] migrations = null;

        for (Class c : cls) {
            String sql = "";
            String tableName = "";

            sqlmap = new HashMap<>();
            modifiesMap = new HashMap<>();

            //先获取表名
            AnnoTableName annoTableName = (AnnoTableName) c.getAnnotation(AnnoTableName.class);
            if (annoTableName != null && !TextUtils.isEmpty(annoTableName.value())) {
                tableName = annoTableName.value();
            } else {
                tableName = c.getName();
            }

            //判断是否有需要改表的标签
            Annotation[] as = c.getAnnotations();
            for (Annotation t : as) {
                if (t.annotationType().equals(AnnoModifyTables.class)) {
                    AnnoModifyTables modifies = (AnnoModifyTables) t;
                    for (AnnoModifyTable mod : modifies.value()) {
                        if (!modifiesMap.containsKey(mod.newVer())) {
                            List<String> list = new ArrayList<String>();
                            modifiesMap.put(mod.newVer(),list);
                            modifiesMap.get(mod.newVer()).add(mod.sqlExuce().replace(tableName, tableName + "_TMP"));
                        } else {
                            throw new Exception("In the same version mutiny modify table sql exist,only one allow exist!");
                        }
                    }
                }
            }

            as = c.getAnnotations();
            for (Annotation t : as) {
                if (t.annotationType().equals(AnnoModifyTable.class)) {
                    AnnoModifyTable modifies = (AnnoModifyTable) t;

                    if (!modifiesMap.containsKey(modifies.newVer())) {
                        List<String> list = new ArrayList<String>();
                        modifiesMap.put(modifies.newVer(),list);
                        modifiesMap.get(modifies.newVer()).add(modifies.sqlExuce().replace(tableName, tableName + "_TMP"));
                    } else {
                        throw new Exception("In the same version mutiny modify table sql exist,only one allow exist!");
                    }

                }
            }

            Map<Integer,List<AlterTable.Action>> verMaps = new HashMap<>();

            //判断字段增加标签
            for (Annotation t : as) {
                if (t.annotationType().equals(AnnoAlertTable.class)) {
                    AnnoAlertTable item = (AnnoAlertTable) t;
//                    if (item.action() == AlterTable.Action.Add) {
//                        packSQL4AddItem(tableName,
//                                item.oldVer(), item.newVer(), item.colName(), item.colType());
//                    } else {
//                        chekVer(item.newVer());
//                        //reBuildTable(tableName, item.newVer());
//                    }

                    if(!verMaps.containsKey(item.newVer())){
                        List<AlterTable.Action> alertInfo = new ArrayList<>();
                        alertInfo.add(item.action());
                        verMaps.put(item.newVer(), alertInfo);
                    }

                    verMaps.get(item.newVer()).add(item.action());

                }
                if (t.annotationType().equals(AnnoAlertTables.class)) {
                    AnnoAlertTables table = (AnnoAlertTables) t;
                    for (AnnoAlertTable item : table.value()) {
//                        if (item.action() == AlterTable.Action.Add) {
//                            packSQL4AddItem(tableName,
//                                    item.oldVer(), item.newVer(), item.colName(), item.colType());
//                        } else {
//                            chekVer(item.newVer());
//                            //reBuildTable(tableName, item.newVer());
//                        }
                        if(!verMaps.containsKey(item.newVer())){
                            List<AlterTable.Action> alertInfo = new ArrayList<>();
                            alertInfo.add(item.action());
                            verMaps.put(item.newVer(), alertInfo);
                        }

                        verMaps.get(item.newVer()).add(item.action());
                    }
                }
            }

            for(int i=2;i<=newVer;i++){
                if(modifiesMap.containsKey(i)){
                    if(verMaps.get(i)==null){
                        throw new Exception("need Add AnnoAlterTable to remark what have been change to table in this new version");
                    }
                    boolean changgTable = false;
                    for(AlterTable.Action action:verMaps.get(i)){
                        if(action == AlterTable.Action.Del || action == AlterTable.Action.Mod){
                            changgTable = true;
                        }
                    }
                    if(!changgTable){
                        throw new Exception("need Add AnnoAlterTable to remark what have been change to table in this new version");
                    }
                }
            }

            //只有增加字段
            Field[] fields = c.getFields();
            List<String> colNames = new ArrayList<>();
            List<String> colNamesOlder = new ArrayList<>();
            for (Field fi : fields) {
                AnnoAlterField fas = fi.getAnnotation(AnnoAlterField.class);


                String colName = fi.getName();
                String colNameOlder = colName;
                if (fas != null) {
                    if(!TextUtils.isEmpty(fas.oldColName())){
                        colNameOlder = fas.oldColName();
                    }

                    if (TextUtils.isEmpty(colName)) {
                        colName = fas.colName();
                    }
                    packSQL4AddItem(tableName,
                            fas.oldVer(), fas.newVer(), colName, fas.colType());
                }

                AnnoIgnoe ignoe = fi.getAnnotation(AnnoIgnoe.class);
                if (ignoe == null) {
                    colNames.add(colName);
                    colNamesOlder.add(colNameOlder);
                }
            }

            //
            for (int i = 2; i <= newVer; i++) {
                if (modifiesMap.containsKey(i)) {
                    copyData2NewTable(tableName, i, colNamesOlder,colNames);
                    reNameTable(tableName, i);
                }

                List<String> lasetSqlList = new ArrayList<>();
                if (modifiesMap.size() != 0 && modifiesMap.get(i) != null && modifiesMap.get(i).size() > 0) {
                    lasetSqlList.addAll(modifiesMap.get(i));
                }

                if (sqlmap.size() != 0 && sqlmap.get(i) != null && sqlmap.get(i).size() > 0) {
                    lasetSqlList.addAll(sqlmap.get(i));
                }

                if(lasetSqlList.size()!=0) {
                    list.add(new MyMigration(i - 1, i, lasetSqlList) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {
                            this.excude(database);
                        }
                    });
                }
            }
        }

        if (list.size() != 0) {
            migrations = new Migration[list.size()];
            System.arraycopy(list.toArray(), 0, migrations, 0, list.size());
        } else {
            migrations = new Migration[1];
            migrations[0] = new MyMigration(0, 0, null) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {

                }
            };
        }

        return migrations;
    }

//    public Migration[] create(int oldVer,int newVer,Class... arryCls) throws Exception{
//        Migration[] migrations = null;
//        //ArrayList<Migration> listOfMigration = new ArrayList<>();
//        for(int i=1;i<=newVer;i++) {
//            for (Class cls : arryCls) {
//                Migration tmpMig = new MyMigration(cls, oldVer, newVer) {
//
//                    @Override
//                    public void migrate(@NonNull SupportSQLiteDatabase database) {
//                        this.excude(database, 1);
//                    }
//                };
//                list.add(tmpMig);
//            }
//        }
//
//        if (list.size() != 0) {
//            migrations = new Migration[list.size()];
//            System.arraycopy(list.toArray(), 0, migrations, 0, list.size());
//        } else {
//            migrations = new Migration[1];
//            migrations[0] = new MyMigration(0, 0, null) {
//                @Override
//                public void migrate(@NonNull SupportSQLiteDatabase database) {
//
//                }
//            };
//        }
//
//        return migrations;
//    }

    //创建新的临时表
    private void reBuildTable(String table, int newVer) throws Exception {
        if (!modifiesMap.containsKey(newVer)) {
            throw new Exception("if want to modify or delete field need a modify @AnnoModifyTable tag to excude for create a new talbe");
        }

        modifiesMap.get(newVer).add(modifiesMap.get(newVer).get(0).replace(table, table + "_TMP"));
    }

    private void copyData2NewTable(@NonNull String table, @NonNull int newVer, @NonNull List<String> columns, List<String> columnsNew) {
        List<String> list = modifiesMap.get(newVer);
        StringBuffer sql = new StringBuffer("Insert into " +
                table + "_TMP(");
        StringBuffer columnsSql = new StringBuffer();
        StringBuffer columnsNewSql = new StringBuffer();
        for (String column : columns) {
            if (!TextUtils.isEmpty(column)) {
                columnsSql.append(column + ",");
            }
        }
        sql.append(columnsSql.substring(0, columnsSql.length() - 1) + ") ");
        sql.append("select ");
        if (columnsNew != null && columnsNew.size() > 0) {
            for (String column : columnsNew) {
                if (!TextUtils.isEmpty(column)) {
                    columnsNewSql.append(column + ",");
                }
            }
        }
        if(columnsNewSql.length()>1) {
            sql.append(columnsNewSql.substring(0, columnsNewSql.length() - 1));
        }

        sql.append(" from " + table);

        list.add(sql.toString());
    }

    private void reNameTable(@NonNull String table, @NonNull int newVer) {
        modifiesMap.get(newVer).add("drop table if exists " + table);
        modifiesMap.get(newVer).add("alter table " + table + "_TMP rename to " + table);
    }

    //重命名
    //重新建表导数据
    private void packSQL4ModItem(String tableName, int newVer) {

    }

    private void chekVer(int newVer) {
        if (!sqlmap.containsKey(newVer)) {
            sqlmap.put(newVer, new ArrayList<>());
        }
    }

    protected void packSQL4AddItem(String talbeName, int oldVer, int newVer, String colName, AlterTable.Type type) throws Exception {

        String sql = "";
        sql = String.format("ALTER TABLE %s ADD COLUMN %s %s", talbeName, colName, type.toString().toLowerCase(Locale.ROOT));
        if (type.toString().equalsIgnoreCase("NUMERIC") ||
                type.toString().equalsIgnoreCase("INTEGER")) {
            sql += " NOT NULL DEFAULT 0";
        }

        chekVer(newVer);
        sqlmap.get(newVer).add(sql);
    }

    private abstract class MyMigration extends Migration {
        private List<String> sqlList;
        Class clas;

        public MyMigration(int oldv, int newv, List sql) {
            super(oldv, newv);
            if (sql != null && sql.size() > 0) {
                if (this.sqlList == null) {
                    this.sqlList = new ArrayList();
                }
                this.sqlList.addAll(sql);
            }
        }

//        public MyMigration(Class clas,int oldVer,int newVer){
//            super(oldVer,newVer);
//            this.clas = clas;
//        }

        public void excude(SupportSQLiteDatabase database) {
            for (String sql : sqlList) {
                database.execSQL(sql);
            }
        }

        public void excude(SupportSQLiteDatabase database,int t){

        }
    }
}
