package com.thatsmanmeet.tasky.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoDao() : TodoDao

    companion object{
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getInstance(context:Context) : TodoDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                    ).allowMainThreadQueries().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}