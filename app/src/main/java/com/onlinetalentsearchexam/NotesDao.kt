package com.onlinetalentsearchexam

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note :Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notesTable order by timestamp asc")
    fun getAllNotes(): LiveData<List<Note>>

    @Query("Select * from notesTable order by timestamp asc")
    fun getAllData(): List<Note>

    @Query("DELETE FROM notesTable")
    fun clearTable()
}