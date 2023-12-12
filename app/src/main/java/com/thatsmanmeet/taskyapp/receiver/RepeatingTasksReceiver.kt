package com.thatsmanmeet.taskyapp.receiver


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.room.Room
import com.thatsmanmeet.taskyapp.constants.Constants
import com.thatsmanmeet.taskyapp.room.Todo
import com.thatsmanmeet.taskyapp.room.TodoDatabase
import com.thatsmanmeet.taskyapp.room.deletedtodo.DeletedTodoDatabase
import com.thatsmanmeet.taskyapp.screens.scheduleNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RepeatingTasksReceiver : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent!!.action == Intent.ACTION_DATE_CHANGED || intent.action == "repeating_tasks") {
            val todoDatabase = Room.databaseBuilder(
                context!!.applicationContext,
                TodoDatabase::class.java,
                "todo_database"
            ).build()
            val deletedTodoDatabase = Room.databaseBuilder(
                context!!.applicationContext,
                DeletedTodoDatabase::class.java,
                "deleted_todo_database"
            ).build()
            coroutineScope.launch {
                withTimeout(10000) {
                    todoDatabase.todoDao().getAllTodosFlow().collect{todos->
                        for(todo in todos){
                            if ((todo.date!!.isNotEmpty() && todo.time!!.isNotEmpty()) && todo.isRecurring) {
                                val currentDate =
                                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                        Date()
                                    )
                                val newTodo = Todo(
                                    ID = todo.ID,
                                    title = todo.title,
                                    isCompleted = false,
                                    date = currentDate,
                                    time = todo.time,
                                    notificationID = todo.notificationID,
                                    isRecurring = todo.isRecurring,
                                    todoDescription = todo.todoDescription
                                )
                                scheduleNotification(
                                    context,
                                    newTodo.title,
                                    newTodo.todoDescription,
                                    time = "${newTodo.date} ${newTodo.time}",
                                    todo = newTodo
                                )
                                todoDatabase.todoDao().updateTodo(newTodo)
                            }
                        }
                    }
                }
            }
            coroutineScope.launch {
                withTimeout(10000){
                    deletedTodoDatabase.deletedTodoDao().getAllTodosFlow().collect{deletedTodos->
                        val currentDate =
                            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                Date()
                            )
                        for(todo in deletedTodos){
                            if(currentDate == todo.todoDeletionDate){
                                deletedTodoDatabase.deletedTodoDao().deleteTodo(todo)
                            }
                        }
                    }
                }
            }
            val nextAlarmIntent = Intent(context.applicationContext,RepeatingTasksReceiver::class.java).also {
                it.action = "repeating_tasks"
            }
            val currentAlarmTime = System.currentTimeMillis()
            val nextAlarmTime = currentAlarmTime + (24 * 60 * 60 * 1000) // Add 24 hours
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                nextAlarmTime,
                PendingIntent.getBroadcast(context.applicationContext,
                    Constants.BROADCAST_ID,nextAlarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            )
        }
    }
}