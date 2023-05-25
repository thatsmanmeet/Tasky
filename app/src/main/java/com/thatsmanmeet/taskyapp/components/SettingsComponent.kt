package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SettingsComponent(
    modifier: Modifier = Modifier,
    settingHeaderText: String,
    settingText:String,
    painterResourceID: Int,
    clickable: ()-> Unit
) {

    Spacer(modifier = modifier.height(12.dp))
    Card(
        modifier = modifier
            .heightIn(56.dp)
            .clip(RoundedCornerShape(15.dp))
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .clickable {
                clickable()
            },
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .weight(1f)
                    .padding(10.dp)
            ){
                Text(
                    text = settingHeaderText,
                    fontSize = 18.sp,

                    )

                Text(
                    text = settingText,
                    fontSize = 10.sp,
                )
            }
            Icon(
                painter = painterResource(painterResourceID),
                contentDescription = "code icon",
                modifier = modifier
                    .size(35.dp)
                    .weight(0.1f),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

}