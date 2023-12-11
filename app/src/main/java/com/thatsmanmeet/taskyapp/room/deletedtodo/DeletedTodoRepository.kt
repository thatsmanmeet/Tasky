package com.thatsmanmeet.taskyapp.room.deletedtodo


import kotlinx.coroutines.flow.Flow

class DeletedTodoRepository(private val deletedTodoDao: DeletedTodoDao) {
        suspend fun insertTodo(todo: DeletedTodo){
            deletedTodoDao.addTodo(todo)
        }

        suspend fun updateTodo(todo: DeletedTodo){
            deletedTodoDao.updateTodo(todo)
        }

        suspend fun deleteTodo(todo: DeletedTodo){
            deletedTodoDao.deleteTodo(todo)
        }

        fun getAllTodosFlow() : Flow<List<DeletedTodo>> = deletedTodoDao.getAllTodosFlow()
}