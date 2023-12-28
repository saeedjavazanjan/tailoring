package com.saeeed.devejump.project.tailoring.presentation.ui.product_detail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.saeeed.devejump.project.tailoring.domain.model.Product
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel
@Inject constructor(

):ViewModel() {

    val loading = mutableStateOf(false)
    val dialogQueue= DialogQueue()
    val product: MutableState<Product?> = mutableStateOf(null)

}