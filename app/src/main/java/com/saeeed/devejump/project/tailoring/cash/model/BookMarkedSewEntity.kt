package com.saeeed.devejump.project.tailoring.cash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookMarkedSewMethods")
data class BookMarkedSewEntity(
// Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

// Value from API
    @ColumnInfo(name = "title")
    var title: String,

// Value from API
    @ColumnInfo(name = "publisher")
    var publisher: String,

// Value from API
    @ColumnInfo(name = "featured_image")
    var featuredImage: String,

// Value from API
    @ColumnInfo(name = "rating")
    var rating: Int,

// Value from API
    @ColumnInfo(name = "source_url")
    var sourceUrl: String,


    @ColumnInfo(name = "ingredients")
    var ingredients: String = "",

    /**
     * Value from API
     */
    @ColumnInfo(name = "date_added")
    var dateAdded: Long,

    /**
     * Value from API
     */
    @ColumnInfo(name = "date_updated")
    var dateUpdated: Long,

    /**
     * The date this recipe was "refreshed" in the cache.
     */
    @ColumnInfo(name = "date_cached")
    var dateCached: Long,
)