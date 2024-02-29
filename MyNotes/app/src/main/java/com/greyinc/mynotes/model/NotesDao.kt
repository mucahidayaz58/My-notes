package com.greyinc.mynotes.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NotesDao {
    @Insert
    suspend fun insert(notes: ArrayList<Notes>)

    @Query("DELETE FROM Notes WHERE id=:id")
    suspend fun delete(id:Int)

    @Query("SELECT * FROM Notes")
    suspend fun getAllNotes(): List<Notes>

    @Query("SELECT * FROM Notes WHERE note LIKE :searchquery")
    suspend fun getAllQuery(searchquery:String):List<Notes>

    @Query("UPDATE Notes SET note=:note,title=:title,date=:date WHERE id=:id")
    suspend fun update(note:String,title:String,date:String,id:Int)

}