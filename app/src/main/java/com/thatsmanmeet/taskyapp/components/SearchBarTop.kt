package com.thatsmanmeet.taskyapp.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchBarTop(searchText: String, onValueChange: (String)->Unit) {
    var searchBarVisible by rememberSaveable {
        mutableStateOf(false)
    }
    //Auto keyboard focus
    val windowInfo = LocalWindowInfo.current
    val focusRequester = remember { FocusRequester() }

    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.padding(horizontal = 10.dp)
        .fillMaxWidth(0.8f)
    ) {
        if(!searchBarVisible){
            IconButton(
                onClick = {
                    searchBarVisible = true
                }
            ) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
        else{

                TextField(
                    value = searchText,//searchText ,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .align(Alignment.CenterVertically)
                        .padding(top = 5.dp, bottom = 5.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        textMotion = TextMotion.Animated,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp
                    ),
                    shape = RoundedCornerShape(30.dp),
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            modifier = Modifier.height(20.dp).width(20.dp)
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (searchText.isNotEmpty()) {
                                onValueChange("")
                            } else {
                                searchBarVisible = false
                            }
                        }) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                modifier = Modifier.height(20.dp).width(20.dp)
                            )
                        }
                    },
                    placeholder = { Text(text = "Search") },
                    singleLine = true
                )
            LaunchedEffect(windowInfo) {
                snapshotFlow { windowInfo.isWindowFocused }.collect { isWindowFocused ->
                    if (isWindowFocused) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SearchBarTopPreview() {
    SearchBarTop(" ") {}
}
