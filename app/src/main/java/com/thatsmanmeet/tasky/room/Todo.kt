package com.thatsmanmeet.tasky.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true) var ID:Long? = null,
    @ColumnInfo(name = "title") var title:String? = "",
    @ColumnInfo(name = "completed") var isCompleted:Boolean = false
)
