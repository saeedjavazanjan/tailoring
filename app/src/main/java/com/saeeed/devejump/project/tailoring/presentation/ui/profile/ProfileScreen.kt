package com.saeeed.devejump.project.tailoring.presentation.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun ProfileScreen(
    author:String=""
){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Text(text = author.toString())
    }
}