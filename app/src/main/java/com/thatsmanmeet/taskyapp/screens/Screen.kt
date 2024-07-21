package com.thatsmanmeet.taskyapp.screens

sealed class Screen(val route:String){
    object MyApp: Screen(route = "myapp_screen")
    object PermissionScreen: Screen("permission_screen")
    object SettingsScreen : Screen("settings_screen")
    object SearchScreen: Screen("search_screen")
    object DeletedTodosScreen:Screen("deleted_todo_screen")
    object AboutScreen:Screen("about_screen")
    object NotesScreen:Screen("notes_screen")
    object AddNotesScreen:Screen("add_notes_screen")
    object EditNotesScreen:Screen("edit_notes_screen/{noteID}")
}
