package com.saeeed.devejump.project.tailoring.cash.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.model.SewEntity

@Database(entities = [SewEntity::class ], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun recipeDao(): SewMethodDao

    companion object {
        val DATABASE_NAME: String = "sew_db"
    }
}

