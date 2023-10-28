package com.onlinetalentsearchexam.database

import androidx.room.*

@Dao
interface AnswerDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Answer?)

    @Query("SELECT * FROM Answer WHERE exam_taken_id =:exam_taken_id")
    suspend fun getAll(exam_taken_id: String?): List<Answer?>?

    @Delete
    suspend fun delete(task: Answer?)

    @Update
    suspend fun update(task: Answer?)


    @Query("DELETE FROM Answer WHERE exam_taken_id =:exam_taken_id")
    suspend fun deleteTestResult(exam_taken_id: String?)



}