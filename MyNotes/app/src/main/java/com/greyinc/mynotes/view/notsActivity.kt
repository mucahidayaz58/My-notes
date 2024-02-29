package com.greyinc.mynotes.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract.Colors
import android.provider.ContactsContract.CommonDataKinds.Note
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import com.greyinc.mynotes.R
import com.greyinc.mynotes.databinding.ActivityNotsBinding
import com.greyinc.mynotes.databinding.RecyclerViewItemBinding
import com.greyinc.mynotes.model.Notes
import com.greyinc.mynotes.model.NotesDatabase
import com.greyinc.mynotes.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

class notsActivity : AppCompatActivity() {
    private lateinit var viewModel:NotesViewModel
    private lateinit var arrayList:ArrayList<Notes>
    private lateinit var binding: ActivityNotsBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNotsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView2.visibility=View.GONE
        binding.imageView3.visibility=View.VISIBLE
        val nt=intent.getStringExtra("not")
        val asil=intent.getStringExtra("asil")
        val nott=intent.getStringExtra("mainnote")
        val baslik=intent.getStringExtra("title")
        viewModel= ViewModelProvider(this).get(NotesViewModel::class.java)
        val intent=intent.getStringExtra("note")

        if (intent.equals("olustur")){
            binding.titleText.text.clear()
            binding.notText.text.clear()
        }else if(intent.equals("update")){
            binding.titleText.setText(baslik)
            binding.notText.setText(nott)
        }else if (intent.equals("flags")){
            binding.textView2.setText("Not Oku")
            binding.warn.setText("Not: Bu modda iken düzenleme/değiştirme yapamazsınız.")
            binding.warn.setTextColor(Color.RED)
            binding.titleText.setText(asil)
            binding.notText.setText(nt)
            binding.titleText.setEnabled(false)
            binding.notText.setEnabled(false)
        }

        binding.imageView2.setOnClickListener {
            if (intent.equals("update")){
                updateNote()
                finish()
            }else{
                saveNot()
                finish()
            }

        }

        binding.imageView3.setOnClickListener {
            finish()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

        binding.notText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("beforetext")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.notText.text.toString().length>0){
                    binding.imageView2.visibility=View.VISIBLE
                }else{
                    binding.imageView2.visibility=View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                println("aftertext")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
    fun saveNot(){
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-dd-MM HH:mm")
        val date = formatter.format(time)
        var titlee=binding.titleText.text.toString()
        if (titlee.length<=0){
            titlee="Empty"
        }
        val not=binding.notText.text.toString()
        val notes=Notes(not,titlee,date.toString())
        arrayList=ArrayList<Notes>()
        arrayList.add(notes)
        //ekleme işlemi tamam
        viewModel.saveNoter(this,arrayList)
        val intent= Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

    fun updateNote(){
        var titlee=binding.titleText.text.toString()

        if (titlee.length<=0){
            titlee="Empty"
        }
        val notee=binding.notText.text.toString()
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-dd-MM HH:mm")
        val date = formatter.format(time)+" (Düzenlendi)"
        val intent=intent
        val deger=intent.getStringExtra("note")
        if (deger=="update"){
            val intIntent=intent.getIntExtra("id",0)
            viewModel.updateNote(this,notee,titlee,date,intIntent)
            val intent3=Intent(this,MainActivity::class.java)
            startActivity(intent3)

        }
    }

}