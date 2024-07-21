package com.thatsmanmeet.taskyapp.room.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note (
    @PrimaryKey(autoGenerate = true) var ID:Long? = null,
    @ColumnInfo(name = "title") var title:String? = "",
    @ColumnInfo(name = "body") var body : String? = "",
    @ColumnInfo(name = "date") var date:String? = "",
    @ColumnInfo(name = "favourite", defaultValue = "") var isFavourite : Boolean? = false,
    )