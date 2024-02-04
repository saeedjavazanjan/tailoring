package com.saeeed.devejump.project.tailoring.cash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.saeeed.devejump.project.tailoring.domain.model.CommentOnSpecificPost
import com.saeeed.devejump.project.tailoring.domain.model.UserData

@Entity(tableName = "userData")
data class UserDataEntity(
// Value from API
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

    @ColumnInfo(name="likes")
    var liKes: String,

    @ColumnInfo(name="bookMarks")
    var bookMarks: String,



)








