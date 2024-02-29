package com.greyinc.mynotes.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.greyinc.mynotes.model.Notes
import com.greyinc.mynotes.model.NotesDatabase
import com.greyinc.mynotes.view.MainActivity
import kotlinx.coroutines.launch

class NotesViewModel(application: Application): NotesDatabaseViewModel(application) {
    var notes=MutableLiveData<ArrayList<Notes>>()
    val obserNote= MutableLiveData<ArrayList<Notes>>()
    lateinit var veriler:List<Notes>
    lateinit var noteData: List<Notes>



    fun refreshData(context: Context){
        launch {
            val dao=NotesDatabase(context).notesDAO()
            veriler=dao.getAllNotes()
            notes.value= veriler as ArrayList<Notes>
        }
    }


    fun saveNoter(context: Context,notesList:ArrayList<Notes>){
        launch {
            val dao=NotesDatabase(context).notesDAO()
            dao.insert(notesList)
        }
        }

    fun deleteNote(context: Context,id:Int){
        launch {
            val dao=NotesDatabase(context).notesDAO()
            dao.delete(id)
        }
    }

    fun getAllQuery(context:Context,query:String){
        launch {
            val dao=NotesDatabase(context).notesDAO()
            noteData=dao.getAllQuery(query)
            obserNote.value=noteData as ArrayList<Notes>
        }
    }

    fun updateNote(context: Context,note:String,title:String,date:String,id: Int){
        launch {
            val dao=NotesDatabase(context).notesDAO()
            dao.update(note,title,date,id)
        }
    }
}