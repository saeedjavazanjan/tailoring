package com.saeeed.devejump.project.tailoring.network.model

import com.google.gson.annotations.SerializedName

data class RegisterUserPasswordDto(

	@field:SerializedName("UserName")
	val userName: String? = null,

	@field:SerializedName("PhoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("PassWord")
	val passWord: String? = null
)
