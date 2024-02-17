package com.saeeed.devejump.project.tailoring.network.response

import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.network.model.UserDataDto

data class LoginResponse(

	@field:SerializedName("userData")
	val userData: UserDataDto? = null,

	@field:SerializedName("token")
	val token: String? = null
)


