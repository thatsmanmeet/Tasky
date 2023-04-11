package com.thatsmanmeet.taskyapp.reciever

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.thatsmanmeet.taskyapp.room.TodoDatabase
import com.thatsmanmeet.taskyapp.screens.scheduleNotification

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Intent.ACTION_BOOT_COMPLETED){


            val todoDatabase = Room.databaseBuilder(
                context!!.applicationContext,
                TodoDatabase::class.java,
                "todo_database"
            ).build()

            val todoList = todoDatabase.todoDao().getAllTodos()

            todoList.observeForever {todos->
                todos.forEach {todo->
                    if(todo.date!!.isNotEmpty() && todo.time!!.isNotEmpty()){
                        scheduleNotification(
                            context = context,
                            titleText = todo.title,
                            messageText = "Did you complete your Task ?",
                            time = "${todo.date} ${todo.time}",
                            todo = todo
                        )
                    }
                }
            }

            todoList.removeObserver{

            }
        }
    }
}