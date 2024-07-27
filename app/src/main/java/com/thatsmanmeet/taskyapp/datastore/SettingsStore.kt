package com.thatsmanmeet.taskyapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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
        val SHOW_24_HOUR_CLOCK_KEY = booleanPreferencesKey("show_24_hour_clock_preference")
        val TASK_COMPLETION_SOUNDS = booleanPreferencesKey("sound_list_preference")
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode_preference")
        val USE_SYSTEM_FONT = booleanPreferencesKey("use_system_font_preference")
        val USE_LEGACY_DATE_TIME_PICKERS = booleanPreferencesKey("use_legacy_date_time_pickers")
    }

    val getTaskListKey : Flow<Boolean?> = context.dataStore.data.map {preference->
        preference[TASK_LIST_KEY] ?: true
    }

    val getAnimationKey : Flow<Boolean?> = context.dataStore.data.map {preference->
        preference[ANIMATION_SHOW_KEY] ?: true
    }

    val getClockKey : Flow<Boolean?> = context.dataStore.data.map {preference->
        preference[SHOW_24_HOUR_CLOCK_KEY] ?: true
    }
    val getSoundKey : Flow<Boolean?> = context.dataStore.data.map{preference->
        preference[TASK_COMPLETION_SOUNDS] ?: true
    }

    val getThemeModeKey : Flow<String?> = context.dataStore.data.map {preference ->
        preference[THEME_MODE_KEY] ?: ""
    }

    val getUseSystemFontKey : Flow<Boolean?> = context.dataStore.data.map {preference ->
        preference[USE_SYSTEM_FONT] ?: false
    }

    val getUseLegacyDateTimePickers: Flow<Boolean?> = context.dataStore.data.map {preference ->
        preference[USE_LEGACY_DATE_TIME_PICKERS] ?: false
    }


    suspend fun saveUseLegacyDateTimePickers(isEnabled: Boolean){
        context.dataStore.edit {preferences->
            preferences[USE_LEGACY_DATE_TIME_PICKERS] = isEnabled
        }
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

    suspend fun saveSoundPlayKey(isEnabled: Boolean){
        context.dataStore.edit {preferences->
            preferences[TASK_COMPLETION_SOUNDS] = isEnabled
        }
    }

    suspend fun saveSystemFontsKey(isEnabled: Boolean){
        context.dataStore.edit {preferences->
            preferences[USE_SYSTEM_FONT] = isEnabled
        }
    }

    suspend fun saveClockKey(isEnabled: Boolean){
        context.dataStore.edit { preference->
            preference[SHOW_24_HOUR_CLOCK_KEY] = isEnabled
        }
    }

    suspend fun saveThemeModeKey(mode:String){
        context.dataStore.edit { preference->
            preference[THEME_MODE_KEY] = mode
        }
    }
}
