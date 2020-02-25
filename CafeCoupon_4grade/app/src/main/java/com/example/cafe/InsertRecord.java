package com.example.cafe;

import android.database.Cursor;

import static com.example.cafe.MainActivity.db;
import static com.example.cafe.MainActivity.tableCreated;

/**
 * Created by Dr.kim on 2017-03-31.
 * // 데이터 처리하는 클래스
 */

public class InsertRecord {

    public static void createTable() {
        //db.execSQL("DROP TABLE IF EXISTS "+ name);
        db.execSQL("CREATE TABLE IF NOT EXISTS location (" + "_id integer PRIMARY KEY autoincrement," + "x text," + "y text);");
        tableCreated = true;
    }


    public static void createTable2() {
        db.execSQL("CREATE TABLE IF NOT EXISTS push (" + "_id integer PRIMARY KEY autoincrement," + "pushState text);");
        db.execSQL("insert into push (pushState) values ('0');");
        tableCreated = true;
    }

    public static void createTable3(String name) {
        db.execSQL("CREATE TABLE IF NOT EXISTS gameChecker (" + "_id integer PRIMARY KEY autoincrement," + "checkGameState integer," + "GameTimer text," + "GameDate text);");
        db.execSQL("insert into " + name + "(checkGameState, GameTimer, GameDate) values (1,'00:00','04-01');");
    }

    public static void insertRecord(String x, String y) {

        db.execSQL("insert into location (x, y) values ('" + x + "','" + y + "');");

    }

    public static void insertRecord2(String x) {

        db.execSQL("insert into push (pushState) values ('" + x + "');");

    }

    public static void insertRecordCall(int state, String number, String Date, String calloutTime) {
        db.execSQL("insert into phonelist (callState, phoneNumber, callDate, callOutTime) values ('" + state + "', '" + number + "','" + Date + "','" + calloutTime + "');");
    }

    public static void deleteRecord(String table, int pos) {
        db.execSQL("delete from " + table + " where _id=" + pos);
    }

    public static void allDeleteRecord(String table) {
        db.execSQL("delete from " + table);
    }

    public static String stateTestX() {
        String x = "";
        Cursor cursor = db.rawQuery("select *from location", null);
        while (cursor.moveToNext()) {
            x = cursor.getString(1);
        }

        return x;
    }

    public static String stateTestY() {
        String y = "";
        Cursor cursor = db.rawQuery("select *from location", null);
        while (cursor.moveToNext()) {
            y = cursor.getString(2);
        }

        return y;
    }

    public static String pushState() {
        String y = "";
        Cursor cursor = db.rawQuery("select *from push", null);
        while (cursor.moveToNext()) {
            y = cursor.getString(1);
        }

        return y;
    }

    public static String dateChecker() {
        String date = "";
        Cursor cursor = db.rawQuery("select *from gameChecker", null);
        while (cursor.moveToNext()) {
            date = cursor.getString(3);
        }

        return date;
    }

}
