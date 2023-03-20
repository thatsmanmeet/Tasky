package com.thatsmanmeet.tasky.screens

sealed class Screen(val route:String){
    object MyApp: Screen(route = "myapp_screen")
    object PermissionScreen:Screen("permission_screen")
}
