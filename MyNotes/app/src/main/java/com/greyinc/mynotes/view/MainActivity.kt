package com.greyinc.mynotes.view


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.greyinc.mynotes.Adapter.GridViewAdapter
import com.greyinc.mynotes.R
import com.greyinc.mynotes.databinding.ActivityMainBinding
import com.greyinc.mynotes.databinding.RecyclerViewItemBinding
import com.greyinc.mynotes.model.NotesDatabase
import com.greyinc.mynotes.viewmodel.NotesViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding:ActivityMainBinding
    lateinit var recyclerViewItemBinding: RecyclerViewItemBinding
    private lateinit var viewModel:NotesViewModel
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding= ActivityMainBinding.inflate(layoutInflater)
        recyclerViewItemBinding= RecyclerViewItemBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        val database= NotesDatabase.databaseOlustur(this)
        viewModel=ViewModelProvider(this).get(NotesViewModel::class.java)
        mainBinding.imageView4.visibility=View.GONE
        obserLiveData()
        obserLiveDataTwo()
        readData()
        (viewModel.notes.observe(this, Observer {
            if (it.size<=0){
                mainBinding.imageView4.visibility=View.VISIBLE
            }
            println(it.size)
        }))

        sharedPreferences=getSharedPreferences("data", Context.MODE_PRIVATE)
        val edit=sharedPreferences.edit()


        mainBinding.switch1.setOnClickListener {
            edit.apply{
                putBoolean("NIGHTOPEN",mainBinding.switch1.isChecked)
            }.apply()

            if (mainBinding.switch1.isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


    }

    fun obserLiveData(){
        viewModel.refreshData(this)
        viewModel.notes.observe(this, Observer {
            val g_view=findViewById<GridView>(R.id.gridView)
            val baseAdapter=GridViewAdapter(it,this,this)
            g_view.adapter=baseAdapter
        })
    }


    fun addNote(view: View){
        val intent=Intent(this,notsActivity::class.java)
        intent.putExtra("olustur","olustur")
        startActivity(intent)
    }

    fun obserLiveDataTwo(){
        val txt=findViewById<SearchView>(R.id.editTextText)
        txt.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                viewModel.getAllQuery(this@MainActivity,p0+"%")
                viewModel.obserNote.observe(this@MainActivity, Observer {
                    val g_view=findViewById<GridView>(R.id.gridView)
                    val baseAdapter=GridViewAdapter(it,this@MainActivity,this@MainActivity)
                    g_view.adapter=baseAdapter
                })
                return true
            }


        })
    }
    fun readData(){
        val preferences=getSharedPreferences("data", Context.MODE_PRIVATE)
        val deger=preferences.getBoolean("NIGHTOPEN",false)
        mainBinding.switch1.isChecked=deger
        if (deger){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}