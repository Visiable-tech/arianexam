package com.onlinetalentsearchexam.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["exam_taken_id", "question_id"], unique = true)])
data class Answer (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "exam_taken_id") val exam_taken_id: String?,
    @ColumnInfo(name = "question_id") val question_id: String?,
    @ColumnInfo(name = "answer_id") val answer_id: String?,
    @ColumnInfo(name = "position") val position: Int?
        )
