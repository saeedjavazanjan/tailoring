package com.saeeed.devejump.project.tailoring.cash

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.saeeed.devejump.project.tailoring.cash.model.CommentEntity
import com.saeeed.devejump.project.tailoring.cash.model.FollowersEntity
import com.saeeed.devejump.project.tailoring.cash.model.FollowingEntity
import com.saeeed.devejump.project.tailoring.cash.model.PostEntity
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntity
import com.saeeed.devejump.project.tailoring.cash.relations.CommentsByPostId
import com.saeeed.devejump.project.tailoring.utils.FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
import com.saeeed.devejump.project.tailoring.utils.POSTS_PAGINATION_PAGE_SIZE

@Dao
interface SewMethodDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSew(sewPost: PostEntity): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserData(userData: UserDataEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Transaction
    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostWithComment(postId: Int): List<CommentsByPostId>

   /* @Transaction
    @Query("SELECT * FROM userData WHERE userid = :userID")
    suspend fun getUserFollowers(userID: Int): List<FollowersByConsideredUserId>

    @Transaction
    @Query("SELECT * FROM userData WHERE userid = :userID")
    suspend fun getUserFollowings(userID: Int): List<FollowingsByConsideredUserId>*/
    @Query("SELECT * FROM userData WHERE userid = :id")
    suspend fun getUserData(id: Int):UserDataEntity


    @Update
    suspend fun updateUserData(user:List<UserDataEntity>):Int


    @Query("UPDATE userData SET bookMarks = :bookmarkState WHERE userid LIKE :id ")
    suspend fun updateBookmarks(bookmarkState:String,id:Int):Int

    @Query("UPDATE userData SET likes = :likedPostsId WHERE userid LIKE :userId ")
    suspend fun updateLikes(likedPostsId:String,userId:Int):Int

   /* @Query("UPDATE userData SET comments = :comments WHERE userid LIKE :userId ")
    suspend fun updateUserComments(comments:String,userId:Int):Int*/

 /*   @Query("UPDATE sewMethods SET comments = :comments WHERE id LIKE :postId ")
    suspend fun updateCommentsOnPost(comments:String,postId:Int):Int*/

    @Query("UPDATE posts SET `like` = :likeCount WHERE id LIKE :postId ")
    suspend fun updatePostLikeCount(likeCount:String,postId:Int):Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSewMethods(post: List<PostEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFollowers(follower: List<FollowersEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFollowings(following: List<FollowingEntity>): LongArray
   /* @Query("SELECT EXISTS(SELECT bookMars FROM userData WHERE id = :id)")
    fun isBookMarkedOrNot(id : Int) : Boolean*/
    /*@Query("SELECT id FROM bookMarkedSewMethods WHERE id = :id")
    suspend fun getBookMarkedSewById(id: Int): Int*/
    @Query("SELECT * FROM posts WHERE id = :id")
    suspend fun getSewById(id: Int): PostEntity?

   /* @Query("DELETE FROM userData WHERE id IN (:ids)")
    suspend fun deleteSew(ids: List<Int>): Int*/

    @Query("DELETE FROM posts")
    suspend fun deleteAllSewMethods()

    @Query("DELETE FROM posts WHERE id = :primaryKey")
    suspend fun deleteSew(primaryKey: Int): Int

    /**
     * Retrieve recipes for a particular page.
     * Ex: page = 2 retrieves recipes from 30-60.
     * Ex: page = 3 retrieves recipes from 60-90
     */
    @Query("""
        SELECT * FROM posts 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'  
        OR category LIKE  '%' || :query || '%'
        ORDER BY date_added DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """)
    suspend fun searchSewMethods(
        query: String,
        page: Int,
        pageSize: Int = POSTS_PAGINATION_PAGE_SIZE
    ): List<PostEntity>


    @Query("""
        SELECT * FROM followers 
        WHERE user_name LIKE '%' || :query || '%'
        ORDER BY user_name DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """)
    suspend fun searchFollowers(
        query: String,
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowersEntity>

    /**
     * Same as 'searchRecipes' function, but no query.
     */
    @Query("""
        SELECT * FROM posts 
        ORDER BY date_added DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """)
    suspend fun getAllSewMethods(
        page: Int,
        pageSize: Int = POSTS_PAGINATION_PAGE_SIZE
    ): List<PostEntity>

    @Query("""
        SELECT * FROM followers 
        ORDER BY user_name DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """)
    suspend fun getAllFollowers(
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowersEntity>

    @Query("""
        SELECT * FROM followings
        ORDER BY user_name DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """)
    suspend fun getAllFollowings(
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowingEntity>

    /**
     * Restore Recipes after process death
     */
    @Query("""
        SELECT * FROM posts 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%' 
        OR category LIKE  '%' || :query || '%' 
        ORDER BY date_added DESC LIMIT (:page * :pageSize)
        """)
    suspend fun restoreSewMethods(
        query: String,
        page: Int,
        pageSize: Int = POSTS_PAGINATION_PAGE_SIZE
    ): List<PostEntity>


    @Query("""
        SELECT * FROM followers 
        WHERE user_name LIKE '%' || :query || '%'
        ORDER BY user_name DESC LIMIT (:page * :pageSize)
        """)
    suspend fun restoreFollowers(
        query: String,
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowersEntity>

    @Query("""
        SELECT * FROM followings 
        WHERE user_name LIKE '%' || :query || '%'
        ORDER BY user_name DESC LIMIT (:page * :pageSize)
        """)
    suspend fun restoreFollowings(
        query: String,
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowingEntity>

    /**
     * Same as 'restoreRecipes' function, but no query.
     */
    @Query("""
        SELECT * FROM posts 
        ORDER BY date_added DESC LIMIT (:page * :pageSize)
    """)
    suspend fun restoreAllSewMethods(
        page: Int,
        pageSize: Int = POSTS_PAGINATION_PAGE_SIZE
    ): List<PostEntity>

    @Query("""
        SELECT * FROM followers 
        ORDER BY user_name DESC LIMIT (:page * :pageSize)
    """)
    suspend fun restoreAllFollowers(
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowersEntity>

    @Query("""
        SELECT * FROM followers 
        ORDER BY user_name DESC LIMIT (:page * :pageSize)
    """)
    suspend fun restoreAllFollowings(
        page: Int,
        pageSize: Int = FOLLOWERS_OR_FOLLOWING_PAGINATION_PAGE_SIZE
    ): List<FollowingEntity>
}

