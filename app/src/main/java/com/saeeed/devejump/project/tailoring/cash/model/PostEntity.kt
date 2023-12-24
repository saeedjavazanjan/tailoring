package com.saeeed.devejump.project.tailoring.cash.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
// Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

// Value from API
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "post_type")
    var postType: String,

// Value from API
    @ColumnInfo(name = "publisher")
    var publisher: String,

    @ColumnInfo(name = "author_id")
    var authorId: Int,
// Value from API
    @ColumnInfo(name = "featured_image")
    var featuredImage: String,

// Value from API
    @ColumnInfo(name = "like")
    var like: Int,

// Value from API
    @ColumnInfo(name = "video")
    var video: String,


    @ColumnInfo(name = "description")
    var description: String = "",

    /**
     * Value from API
     */
    @ColumnInfo(name = "date_added")
    var dateAdded: Long,

    /**
     * Value from API
     */
    @ColumnInfo(name = "have_product")
    var haveProduct: Int,

    /**
     * The date this recipe was "refreshed" in the cache.
     */
    @ColumnInfo(name = "date_cached")
    var dateCached: Long,
)