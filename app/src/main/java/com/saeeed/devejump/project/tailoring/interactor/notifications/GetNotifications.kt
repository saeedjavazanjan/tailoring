package com.saeeed.devejump.project.tailoring.interactor.notifications

import com.saeeed.devejump.project.tailoring.domain.data.DataState
import com.saeeed.devejump.project.tailoring.domain.model.Notification
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import com.saeeed.devejump.project.tailoring.network.model.NotificationMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNotifications(
    private val retrofitService: RetrofitService,
    private val dtoMapper: NotificationMapper,
) {


    fun getFromServer(
        token: String,
        isNetworkAvailable: Boolean

    ):Flow<DataState<List<Notification>>> = flow{
        emit(DataState.loading())
        try {
            val serverData=retrofitService.getNotifications()
            emit(DataState.success(dtoMapper.toDomainList(serverData)))

        }catch (e:Exception){
            emit(DataState.error(e.message?:"خطای ناشناخته"))
        }

    }




}