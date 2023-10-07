package com.thatsmanmeet.taskyapp.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.datastore.SettingsStore
import com.thatsmanmeet.taskyapp.ui.theme.TaskyTheme


@Composable
fun PermissionRequestScreen(
    navHostController: NavHostController,
    requestOnClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val settingsStore = SettingsStore(context)
    val savedThemeKey = settingsStore.getThemeModeKey.collectAsState(initial = "0")
    TaskyTheme(
        darkTheme = when (savedThemeKey.value.toString()) {
            "null" -> {
                isSystemInDarkTheme()
            }
            "0" -> {
                isSystemInDarkTheme()
            }
            "1" -> {false}
            else -> {true}
        }
    ) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    modifier = modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.ic_security),
                    contentDescription = "Permission Required Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = modifier.height(25.dp))
                Text(text = stringResource(R.string.permission_screen_title), fontSize = 30.sp)
                Spacer(modifier = modifier.height(20.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.padding(start = 20.dp, end = 20.dp)
                ){
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "notification permission icon",
                            modifier = modifier.padding(start = 5.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = modifier.width(15.dp))
                        Text(
                            modifier = modifier.padding(start = 10.dp),
                            text = stringResource(R.string.permission_screen_information_text),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Spacer(modifier = modifier.height(20.dp))
                Button(onClick = {
                    requestOnClick()
                }) {
                    Text(text = stringResource(R.string.permission_screen_request_permission_button))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview(){
    PermissionRequestScreen(
        navHostController = rememberNavController(),
        requestOnClick = {

        }
    )
}