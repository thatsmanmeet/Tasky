package com.thatsmanmeet.tasky.room

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow


class TodoRepository(private val todoDao: TodoDao) {

    suspend fun insertTodo(todo: Todo){
        todoDao.addTodo(todo)
    }

    suspend fun updateTodo(todo: Todo){
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo){
        todoDao.deleteTodo(todo)
    }

    fun getAllTodos() : LiveData<List<Todo>> = todoDao.getAllTodos()

}