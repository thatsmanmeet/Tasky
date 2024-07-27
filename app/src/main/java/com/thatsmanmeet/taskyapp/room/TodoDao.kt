package com.thatsmanmeet.taskyapp.room

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: Todo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)

    @Query("SELECT * FROM todo_table ORDER BY ID ASC")
     fun getAllTodos() : LiveData<List<Todo>>

     @Query("SELECT * FROM todo_table ORDER BY ID ASC")
     fun getAllTodosFlow() : Flow<List<Todo>>

     @Query("SELECT * FROM todo_table WHERE ID = :id")
     fun getOneTodo(id:Long) : Todo

}