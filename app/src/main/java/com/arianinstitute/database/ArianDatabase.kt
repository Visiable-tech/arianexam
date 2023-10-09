package com.arianinstitute.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Answer::class], version = 1, exportSchema = false)
abstract class ArianDatabase : RoomDatabase() {


    companion object {
        @Volatile
        private var INSTANCE: ArianDatabase? = null
        fun getInstance(context: Context): ArianDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ArianDatabase::class.java,
                        "ariandb"
                    ).build()
                }
                return instance
            }
        }
    }

    abstract fun getanswerdetails(): AnswerDAO?
}