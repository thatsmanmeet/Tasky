package com.thatsmanmeet.taskyapp.room

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

    fun getOneTodo(id:Long) : Todo = todoDao.getOneTodo(id)

    fun getAllTodosFlow() : Flow<List<Todo>> = todoDao.getAllTodosFlow()
}