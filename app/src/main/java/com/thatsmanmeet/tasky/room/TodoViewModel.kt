package com.thatsmanmeet.tasky.room


import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatsmanmeet.tasky.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : ViewModel() {

    private var repository:TodoRepository
    val getAllTodos : LiveData<List<Todo>>

    init {
        val dao = TodoDatabase.getInstance(application).todoDao()
        repository = TodoRepository(dao)
        getAllTodos = repository.getAllTodos()
    }

    fun insertTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTodo(todo)
        }
    }
    fun updateTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }
    fun deleteTodo(todo: Todo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

    fun playCompletedSound(context: Context){
        val mp:MediaPlayer = MediaPlayer.create(context, R.raw.completed)
        mp.start()
        mp.setOnCompletionListener {
            mp.stop()
            mp.reset()
            mp.release()
        }
    }

    fun playDeletedSound(context: Context){
        val mp:MediaPlayer = MediaPlayer.create(context, R.raw.deleted)
        mp.start()
        mp.setOnCompletionListener {
            mp.stop()
            mp.reset()
            mp.release()
        }
    }
}