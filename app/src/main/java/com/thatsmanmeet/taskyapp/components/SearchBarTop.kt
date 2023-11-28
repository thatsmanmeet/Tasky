package com.thatsmanmeet.taskyapp.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thatsmanmeet.taskyapp.R

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
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .padding(top = 10.dp, bottom = 10.dp)
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        textMotion = TextMotion.Animated,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 18.sp
                    ),
                    shape = RoundedCornerShape(20.dp),
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
@Preview
@Composable
fun SearchBarTopPreview() {
    SearchBarTop(" ", {})
}
