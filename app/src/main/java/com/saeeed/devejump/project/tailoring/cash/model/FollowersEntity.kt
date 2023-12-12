package com.saeeed.devejump.project.tailoring.cash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "followers")

data class FollowersEntity(
@PrimaryKey(autoGenerate = false)
@ColumnInfo(name = "userid")
var userId: Int,

@ColumnInfo(name="user_name")
var userName: String,

@ColumnInfo(name="phone_number")
var phoneNumber: String,

@ColumnInfo(name="avatar")
var avatar: String,

@ColumnInfo(name="bio")
var bio: String,
@ColumnInfo(name="followers")
var followers: Int,

@ColumnInfo(name="following")
var following: Int,

@ColumnInfo(name="considered_user_id")
var consideredUserId: Int,

)