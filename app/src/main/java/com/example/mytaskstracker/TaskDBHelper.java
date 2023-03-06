package com.example.mytaskstracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "mytasks.db";
        private static final int DATABASE_VERSION = 3;

        private static final String CREATE_TABLE_TASK =
                "create table task (_id integer primary key autoincrement, "
                        + "tasksubject text not null, taskdescription text, taskduedate text);";

        public TaskDBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_TASK);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
}
