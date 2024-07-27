package com.thatsmanmeet.taskyapp.room


import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thatsmanmeet.taskyapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : ViewModel() {

    private var repository: TodoRepository
    val getAllTodosFlow : Flow<List<Todo>>
    val isAnimationPlayingState = mutableStateOf(false)
    private var mediaPlayer : MediaPlayer? = null

    init {
        val dao = TodoDatabase.getInstance(application).todoDao()
        repository = TodoRepository(dao)
        getAllTodosFlow = repository.getAllTodosFlow()
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

    fun getOneTodo(id:Long) : Todo{
       return repository.getOneTodo(id)
    }

    fun playCompletedSound(context: Context){
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context,R.raw.completed)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private var lastDeletedTodo = Todo()

    fun setLastDeletedTodo(todo: Todo){
        lastDeletedTodo = todo
    }

    fun getLastDeletedTodo():Todo{
        return lastDeletedTodo
    }

    fun resetLastDeletedTodo(){
        lastDeletedTodo = Todo()
    }

    fun playDeletedSound(context: Context) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(context, R.raw.deleted)
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }
}