package com.thatsmanmeet.taskyapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun PermissionRequestScreen(
    navHostController: NavHostController,
    requestOnClick: () -> Unit
){
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = Icons.Default.Check,
                contentDescription = null)
            Text(text = "Permission Required", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ){
                Text(
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp),
                    text = "Starting from Android 13+, apps requires Notification Permission to show notifications.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                requestOnClick()
            }) {
                Text(text = "Request Permission")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    PermissionRequestScreen(navHostController = rememberNavController()) {}
}