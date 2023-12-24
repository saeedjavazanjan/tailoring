package com.saeeed.devejump.project.tailoring.cash.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.CommentEntity
import com.saeeed.devejump.project.tailoring.cash.model.FollowersEntity
import com.saeeed.devejump.project.tailoring.cash.model.FollowingEntity
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntity
import com.saeeed.devejump.project.tailoring.cash.model.PostEntity

@Database(
    entities = [
        PostEntity::class,
        UserDataEntity::class,
               CommentEntity::class,
               FollowersEntity::class,
               FollowingEntity::class],
    version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun recipeDao(): SewMethodDao

  //  abstract fun bookMark():BookMarkedSewEntity

    companion object {
        val DATABASE_NAME: String = "sew_db"
    }
}

