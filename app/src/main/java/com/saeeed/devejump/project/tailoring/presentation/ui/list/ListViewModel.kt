package com.saeeed.devejump.project.tailoring.presentation.ui.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.SewMethod
import com.saeeed.devejump.project.tailoring.repository.SewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ListViewModel
    @Inject constructor(
        private val repository: SewRepository,
        @Named("auth_token") private val token: String,

        ):ViewModel() {

    val methods: MutableState<List<SewMethod>> = mutableStateOf(ArrayList())
    val selectedCategory: MutableState<Category?> = mutableStateOf(null)
    var categoryScrollPosition: Float = 0F
    val loading = mutableStateOf(false)

    val query = mutableStateOf("")

    init {
        newSearch()
    }

    fun newSearch(){
        viewModelScope.launch {
            loading.value = true

            resetSearchState()

            delay(1000)


            val result = repository.search(
                token = token,
                page = 1,
                query = "Chicken"
            )
            methods.value = result
            loading.value=false
        }
    }
    private fun resetSearchState(){
        methods.value = listOf()
        if(selectedCategory.value?.value != query.value) clearSelectedCategory()
    }

    private fun clearSelectedCategory(){
        selectedCategory.value = null
    }
    fun onQueryChanged(query: String){
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String){
        val newCategory = getCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }
    fun onChangeCategoryScrollPosition(position: Float){
        categoryScrollPosition = position
    }
}