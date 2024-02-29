package com.greyinc.mynotes.Adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.greyinc.mynotes.R
import com.greyinc.mynotes.databinding.ActivityMainBinding
import com.greyinc.mynotes.databinding.ActivityNotsBinding
import com.greyinc.mynotes.model.Notes
import com.greyinc.mynotes.view.notsActivity
import com.greyinc.mynotes.viewmodel.NotesViewModel

class GridViewAdapter(var liste: List<Notes>, var context: Context, var owner: ViewModelStoreOwner): BaseAdapter() {
    private var layoutInflater:LayoutInflater?=null
    private lateinit var notess:TextView
    private lateinit var viewModel:NotesViewModel
    private lateinit var tarih:TextView
    private lateinit var title:EditText

    override fun getCount(): Int {
        return liste.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView=convertView

        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.recycler_view_item, null)
        }
        notess = convertView!!.findViewById(R.id.textView3)
        tarih=convertView!!.findViewById(R.id.tarih)

        if(liste.get(position).note.length>50){
            notess.setText(liste.get(position).note.substring(0,50)+"...")
        }else{
            notess.setText(liste.get(position).note)
        }

        tarih.setText(liste.get(position).date)

        val imageView=convertView.findViewById<ImageView>(R.id.imageView)
        viewModel= ViewModelProvider(owner).get(NotesViewModel::class.java)

        val carv=convertView.findViewById<CardView>(R.id.cardView2)

        carv.setOnClickListener {
            val intent=Intent(convertView.context,notsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("note","flags")
            intent.putExtra("not",liste.get(position).note)
            intent.putExtra("asil",liste.get(position).title)
            context.startActivity(intent)

        }

        imageView.setOnClickListener {
            val popupMenu= PopupMenu(context,it)
            val binding=ActivityMainBinding.inflate(layoutInflater!!)
            var count=1
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.delete->{
                        viewModel.deleteNote(context,liste.get(position).id)
                        viewModel.notes.observe(parent?.context as LifecycleOwner, Observer {
                            if (count<=2){
                                viewModel.refreshData(context)
                                count+=1
                            }
                            val g_view=binding.gridView
                            val baseAdapter=GridViewAdapter(it,context,owner)
                            g_view.adapter=baseAdapter
                        })

                        true
                    }

                    R.id.update->{
                        val intent=Intent(context,notsActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.putExtra("note","update")
                        intent.putExtra("id",liste.get(position).id)
                        intent.putExtra("mainnote",liste.get(position).note)
                        intent.putExtra("title",liste.get(position).title)
                        context.startActivity(intent)

                        true
                    }else->false
                }
            }
            popupMenu.inflate(R.menu.popupmenu)
            popupMenu.show()
        }

        return convertView
    }
}

