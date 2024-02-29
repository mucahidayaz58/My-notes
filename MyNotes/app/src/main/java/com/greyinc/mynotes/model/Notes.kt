package com.greyinc.mynotes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Notes")
class Notes(
    @ColumnInfo(name = "note") var note: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "date") var date: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    var id:Int=0


}