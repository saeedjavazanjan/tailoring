package com.saeeed.devejump.project.tailoring.presentation.ui.bests

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun BestsScreen(
    type:String?

){
    
    Text(text = type.toString())
}