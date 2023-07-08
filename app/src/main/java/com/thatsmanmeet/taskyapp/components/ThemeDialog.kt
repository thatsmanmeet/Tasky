package com.thatsmanmeet.taskyapp.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.thatsmanmeet.taskyapp.R
import com.thatsmanmeet.taskyapp.misc.AppTheme


@Composable
fun ThemeChangerDialog(
    modifier: Modifier = Modifier,
    selectedItem:MutableState<String>,
    isShowing:MutableState<Boolean>,
    onClick : (String) -> Unit
) {
    if(isShowing.value){
        AlertDialog(
            onDismissRequest = {
                isShowing.value = false
            },
            title = {
                Text(text = stringResource(R.string.select_app_theme_text))
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    val list = mutableListOf(
                        AppTheme("0","System Default",false, R.drawable.ic_phone),
                        AppTheme("1","Light",false,R.drawable.ic_light),
                        AppTheme("2","Dark",false,R.drawable.ic_dark)
                    )
                    LazyColumn(modifier = modifier.padding(2.dp)){
                        items(list){theme->
                            ThemeItem(appTheme = theme, selectedThemeId = selectedItem) { selectedThemeId ->
                                selectedItem.value = selectedThemeId
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                         onClick(selectedItem.value)
                         isShowing.value = false
                    },
                    colors = ButtonColors(containerColor = Color(0xFF229E28), contentColor = Color.White, disabledContentColor = Color.White, disabledContainerColor = Color.Gray)
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        isShowing.value = false
                    },
                    colors = ButtonColors(containerColor = Color(0xFFDB4C41), contentColor = Color.White, disabledContentColor = Color.White, disabledContainerColor = Color.Gray)
                ) {
                    Text(text = "Cancel")
                }
            }
        )
    }
}

@Composable
fun ThemeItem(
    modifier: Modifier = Modifier,
    appTheme: AppTheme,
    selectedThemeId: MutableState<String>,
    onThemeItemSelected: (String) -> Unit
) {
    val isSelected = remember(appTheme.id == selectedThemeId.value) {
        mutableStateOf(appTheme.id == selectedThemeId.value)
    }

    Row(
        modifier = modifier.padding(10.dp).fillMaxWidth().clickable {
            onThemeItemSelected(appTheme.id)
        },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = appTheme.icon), contentDescription = null)
            Spacer(modifier = modifier.width(10.dp))
            Text(text = appTheme.mode)
        }
        if (isSelected.value) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF009688)
            )
        }
    }
}