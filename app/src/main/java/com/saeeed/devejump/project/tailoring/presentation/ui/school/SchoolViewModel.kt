package com.saeeed.devejump.project.tailoring.presentation.ui.school

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saeeed.devejump.project.tailoring.domain.model.Article
import com.saeeed.devejump.project.tailoring.domain.model.Banner
import com.saeeed.devejump.project.tailoring.interactor.school.GetSchoolData
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import com.saeeed.devejump.project.tailoring.utils.DialogQueue
import com.saeeed.devejump.project.tailoring.utils.GET_HOME_BANNERS
import com.saeeed.devejump.project.tailoring.utils.GET_SCHOOL_BANNERS
import com.saeeed.devejump.project.tailoring.utils.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class SchoolViewModel
@Inject constructor(
    private val getSchoolData: GetSchoolData,
    @Named("auth_token") private val token: String,
    private val connectivityManager: ConnectivityManager

    ):ViewModel()
{
    private val bannersQuery = GET_SCHOOL_BANNERS

    val loading = mutableStateOf(false)
    val dialogQueue = DialogQueue()
    val banners: MutableState<List<Banner>> = mutableStateOf(ArrayList())
    val articles:MutableState<List<Article>> = mutableStateOf(ArrayList())


     fun getBanners() {
        getSchoolData.getBanner(
            token = token,
            query = bannersQuery,
            connectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading
            dataState.data?.let { list ->
                banners.value = list

            }

            dataState.error?.let { error ->
                // dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
    }

     fun getArticles(){
       getSchoolData.getArticles(
           token=token,
           isNetworkAvailable = connectivityManager.isNetworkAvailable.value
       ) .onEach { dataState ->

           loading.value=dataState.loading
           dataState.data?.let {
               articles.value=it
           }
           dataState.error?.let {
               dialogQueue.appendErrorMessage("خطای ارتباط",it)

           }




       }.catch {
           dialogQueue.appendErrorMessage("خطای ارتباط",it.message.toString())
       }.launchIn(viewModelScope)
    }

}