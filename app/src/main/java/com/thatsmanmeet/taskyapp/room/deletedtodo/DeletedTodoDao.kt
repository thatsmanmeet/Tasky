package com.thatsmanmeet.taskyapp.room.deletedtodo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DeletedTodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: DeletedTodo)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTodo(todo: DeletedTodo)

    @Delete
    suspend fun deleteTodo(todo: DeletedTodo)

    @Query("SELECT * FROM deleted_todo_table ORDER BY ID ASC")
    fun getAllTodos() : LiveData<List<DeletedTodo>>

    @Query("SELECT * FROM deleted_todo_table ORDER BY ID ASC")
    fun getAllTodosFlow() : Flow<List<DeletedTodo>>

}