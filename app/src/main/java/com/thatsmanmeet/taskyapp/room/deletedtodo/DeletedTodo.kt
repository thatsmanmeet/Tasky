package com.thatsmanmeet.taskyapp.room.deletedtodo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deleted_todo_table")
data class DeletedTodo(
    @PrimaryKey(autoGenerate = true) var ID:Long? = null,
    @ColumnInfo(name = "title") var title:String? = "",
    @ColumnInfo(name = "completed") var isCompleted:Boolean = false,
    @ColumnInfo(name = "date") var date:String? = "",
    @ColumnInfo(name = "time") var time:String? = "",
    @ColumnInfo(name = "notificationID", defaultValue = "0") var notificationID:Int = 0,
    @ColumnInfo(name = "is_Recurring", defaultValue = "false") var isRecurring : Boolean = false,
    @ColumnInfo(name = "description", defaultValue = "") var todoDescription : String? = "",
    @ColumnInfo(name = "deletionDate", defaultValue = "") var todoDeletionDate: String ? = ""
)

