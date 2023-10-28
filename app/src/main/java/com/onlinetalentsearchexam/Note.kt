package com.onlinetalentsearchexam

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
class Note (@ColumnInfo(name = "title")val noteTitle :String,
            @ColumnInfo(name = "ansa")val ansA :String,
            @ColumnInfo(name = "ansb")val ansB :String,
            @ColumnInfo(name = "ansc")val ansC :String,
            @ColumnInfo(name = "ansd")val ansD :String,
            @ColumnInfo(name = "QusID")val QusID :String,
            @ColumnInfo(name = "anstxt")val ansTxt :String,
            @ColumnInfo(name = "SelAnsID")val SelAnsID :String,
            @ColumnInfo(name = "timestamp")val timeStamp :String) {
    @PrimaryKey(autoGenerate = true) var id = 0

}