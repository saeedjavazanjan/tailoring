package com.saeeed.devejump.project.tailoring.presentation.ui.product_detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ProductDetailScreen(
    productJson:String?
) {
    
    Text(text = productJson!!)
}