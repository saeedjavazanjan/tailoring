package com.saeeed.devejump.project.tailoring.cash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userActivityOnPost")
data class UserActivityEntity(
// Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name="bookMarkState")
    var bookMarkState: Boolean,

    @ColumnInfo(name="likeState")
    var liKeState: Boolean,

    @ColumnInfo(name="comment")
    var comment: String,
)




/*
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

    */
/**
     * Value from API
     *//*

    @ColumnInfo(name = "date_added")
    var dateAdded: Long,

    */
/**
     * Value from API
     *//*

    @ColumnInfo(name = "date_updated")
    var dateUpdated: Long,

    */
/**
     * The date this recipe was "refreshed" in the cache.
     *//*

    @ColumnInfo(name = "date_cached")
    var dateCached: Long,
)*/
