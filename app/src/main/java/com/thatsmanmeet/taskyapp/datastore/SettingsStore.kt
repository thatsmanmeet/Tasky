package com.thatsmanmeet.taskyapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsStore(
    private val context: Context
) {
    companion object{
        private val Context.dataStore:DataStore<Preferences> by preferencesDataStore("settings")
        val TASK_LIST_KEY = booleanPreferencesKey("task_list_preference")
        val ANIMATION_SHOW_KEY = booleanPreferencesKey("animation_list_preference")
    }

    val getTaskListKey : Flow<Boolean?> = context.dataStore.data.map {preference->
        preference[TASK_LIST_KEY] ?: true
    }

    val getAnimationKey : Flow<Boolean?> = context.dataStore.data.map {preference->
        preference[ANIMATION_SHOW_KEY] ?: true
    }

    suspend fun saveTaskListKey(isEnabled:Boolean) {
        context.dataStore.edit {preferences->
            preferences[TASK_LIST_KEY] = isEnabled
        }
    }
    suspend fun saveAnimationShowKey(isEnabled:Boolean) {
        context.dataStore.edit {preferences->
            preferences[ANIMATION_SHOW_KEY] = isEnabled
        }
    }
}
