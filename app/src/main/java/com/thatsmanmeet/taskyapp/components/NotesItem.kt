package com.thatsmanmeet.taskyapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.thatsmanmeet.taskyapp.room.notes.Note


@Composable
fun NotesItem(modifier: Modifier = Modifier, note: Note,navHostController:NavHostController) {
    Card (
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                navHostController.navigate(route = "edit_notes_screen/" + note.ID)
            }
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .heightIn(min = 60.dp),
    ) {
        Column(
            modifier = modifier.padding(10.dp)
        ) {
            Text(
                text = if (note.title.isNullOrEmpty()) "Empty Title" else note.title!!,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier = modifier.height(8.dp))
            Text(
                text = if (note.body.isNullOrEmpty()) {
                    "Empty Body"
                } else if (note.body!!.length < 50) {
                    note.body!!
                } else {
                    note.body!!.slice(0..49) + "..."
                }, fontSize = 12.sp)
            Spacer(modifier = modifier.height(8.dp))
            Text(text = "Last updated: ${note.date!!}", fontSize = 10.sp)
        }
    }
    Spacer(modifier = modifier.height(10.dp))
}