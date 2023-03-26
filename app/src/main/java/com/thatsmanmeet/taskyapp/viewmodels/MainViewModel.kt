package com.thatsmanmeet.taskyapp.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val permissionDialogQueue = mutableStateListOf<String>()
    
    fun onPermissionResult(
        permission:String,
        isGranted:Boolean
    ){
        if(!isGranted){
            permissionDialogQueue.add(0,permission)
        }
    }
}