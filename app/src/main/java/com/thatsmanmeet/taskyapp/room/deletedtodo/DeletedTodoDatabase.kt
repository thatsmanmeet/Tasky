package com.thatsmanmeet.taskyapp.room.deletedtodo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DeletedTodo::class],
    version = 1
)
abstract class DeletedTodoDatabase : RoomDatabase() {

    abstract fun deletedTodoDao(): DeletedTodoDao

    companion object{
        @Volatile
        private var INSTANCE : DeletedTodoDatabase? = null

        fun getInstance(context: Context): DeletedTodoDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DeletedTodoDatabase::class.java,
                        "deleted_todo_database"
                    ).allowMainThreadQueries().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}