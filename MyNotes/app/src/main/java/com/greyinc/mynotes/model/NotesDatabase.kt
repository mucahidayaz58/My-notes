package com.greyinc.mynotes.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Notes::class), version = 2)
abstract class NotesDatabase:RoomDatabase() {
    abstract fun notesDAO():NotesDao

    companion object{
       @Volatile private var instance:NotesDatabase?=null
        private val lock=Any()

        operator fun invoke(context: Context)= instance?: synchronized(lock){
            instance?: databaseOlustur(context).also {
                instance=it
            }
        }

        fun databaseOlustur(context:Context)= Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,"notesdatabase"
        ).fallbackToDestructiveMigration()
            .build()
    }

}