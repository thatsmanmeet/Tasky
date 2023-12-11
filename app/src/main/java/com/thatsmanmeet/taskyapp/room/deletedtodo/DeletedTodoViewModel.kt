package com.thatsmanmeet.taskyapp.room.deletedtodo

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DeletedTodoViewModel(application: Application) : ViewModel() {

    private var repository:DeletedTodoRepository
    val getAllDeletedTodos: Flow<List<DeletedTodo>>

    init {
        val dao = DeletedTodoDatabase.getInstance(application).deletedTodoDao()
        repository = DeletedTodoRepository(dao)
        getAllDeletedTodos = repository.getAllTodosFlow()
    }

    fun insertDeletedTodo(deletedTodo: DeletedTodo){
        viewModelScope.launch {
            repository.insertTodo(deletedTodo)
        }
    }
    fun deleteDeletedTodo(deletedTodo: DeletedTodo){
        viewModelScope.launch {
            repository.deleteTodo(deletedTodo)
        }
    }

}