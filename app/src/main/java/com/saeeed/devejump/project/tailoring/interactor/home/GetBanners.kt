package com.saeeed.devejump.project.tailoring.interactor.home

import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper

class GetBanners(
    private val retrofitService: RetrofitService,
    private val entityMapper: SewEntityMapper,
    private val dtoMapper: SewMethodMapper,
) {
}