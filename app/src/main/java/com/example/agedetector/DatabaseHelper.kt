package com.example.agedetector

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app_database.db"
        private const val DATABASE_VERSION = 1

        // User table
        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        // Usage table
        private const val TABLE_USAGE = "usage_table"
        private const val COLUMN_USAGE_ID = "usage_id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create user table
        db.execSQL(
            "CREATE TABLE $TABLE_USERS (" +
                    "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT UNIQUE," +
                    "$COLUMN_PASSWORD TEXT)"
        )

        // Create usage table
        db.execSQL(
            "CREATE TABLE $TABLE_USAGE (" +
                    "$COLUMN_USAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT," +
                    "$COLUMN_DATE TEXT," +
                    "$COLUMN_TIME TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USAGE")

        // Recreate the tables
        onCreate(db)
    }

    fun insertUser(username: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun insertUsage(username: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_DATE, currentDate)
            put(COLUMN_TIME, currentTime)
        }
        db.insert(TABLE_USAGE, null, values)
        db.close()
    }
//    fun getAllUsages(username: String): List<UsageData> {
//        val usageList = mutableListOf<UsageData>()
//
//        try {
//            val db = this.readableDatabase
//
//            // Replace "COLUMN_USERNAME = ?" with your actual column name
//            val selection = username
//            val selectionArgs = arrayOf(username)
//
//
//            val cursor = db.query(TABLE_USAGE, null, username, selectionArgs, null, null, null)
//
//            // Check if the cursor is not null and has data
//            if (cursor != null && cursor.moveToFirst()) {
//                do {
//                    // Ensure the columns exist in the cursor
//                    val dateIndex = cursor.getColumnIndex(COLUMN_DATE)
//                    val timeIndex = cursor.getColumnIndex(COLUMN_TIME)
//
//                    if (dateIndex != -1 && timeIndex != -1) {
//                        val date = cursor.getString(dateIndex)
//                        val time = cursor.getString(timeIndex)
//
//                        val usageData = UsageData(username, date, time)
//                        usageList.add(usageData)
//                    }
//                } while (cursor.moveToNext())
//            }
//        } catch (e: Exception) {
//            // Handle exceptions, log or print the error
//            e.printStackTrace()
//        }
//
//        return usageList
//    }


    fun getAllUsages(): List<UsageData> {
        val usageList = mutableListOf<UsageData>()
        val db = this.readableDatabase

        // No need for username filtering, fetch all entries
        val cursor = db.query(TABLE_USAGE, null, null, null, null, null, null)

        try {
            if (cursor.moveToFirst()) {
                do {
                    val username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                    val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                    val time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME))

                    val usageData = UsageData(username, date, time)
                    usageList.add(usageData)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            // Handle exceptions, log or print the error
            e.printStackTrace()
        } finally {
            cursor.close()


        }


        cursor.close()
        return usageList
    }

    fun isUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null)
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getUserPassword(username: String): String? {
        val db = readableDatabase
        val selection = "$COLUMN_USERNAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_PASSWORD), selection, selectionArgs, null, null, null)
        val password: String? = if (cursor.moveToFirst()) {
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
        } else {
            null
        }
        cursor.close()
        return password
    }

}
