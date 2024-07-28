package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsToggleItem(
    modifier: Modifier = Modifier,
    cardText:String = "",
    cardToggleState:MutableState<Boolean?>,
    onToggleAction: (isToggleChecked:Boolean) -> Unit,
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cardText,
                fontSize = 18.sp,
                modifier = modifier.weight(1f)
            )
            cardToggleState.value?.let { it1 ->
                Switch(
                    modifier = modifier.weight(0.2f),
                    checked = it1,
                    onCheckedChange = { isToggleChecked->
                        cardToggleState.value = isToggleChecked
                        onToggleAction(isToggleChecked)
                    })
            }
        }
    }
    Spacer(modifier = modifier.height(12.dp))
}