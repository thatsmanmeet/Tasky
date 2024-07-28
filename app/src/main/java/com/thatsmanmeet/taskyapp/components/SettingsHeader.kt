package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsHeader(
    modifier: Modifier = Modifier,
    text:String,
    iconContent: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(bottom = 5.dp, top = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconContent()
        Spacer(modifier = modifier.width(8.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp
        )
    }
}