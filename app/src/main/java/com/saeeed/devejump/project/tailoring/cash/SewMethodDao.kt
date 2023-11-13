package com.saeeed.devejump.project.tailoring.cash

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saeeed.devejump.project.tailoring.cash.model.SewEntity
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntity
import com.saeeed.devejump.project.tailoring.domain.model.UserData
import com.saeeed.devejump.project.tailoring.utils.RECIPE_PAGINATION_PAGE_SIZE

@Dao
interface SewMethodDao {
    @Insert
    suspend fun insertSew(sewPost: SewEntity): Long

    @Insert
    suspend fun insertUserData(userData: UserDataEntity): Long

    @Query("SELECT * FROM userData WHERE id = :id")
    suspend fun getUserData(id: Int):UserDataEntity

    @Query("UPDATE userData SET bookMars = :bookmarkState WHERE id LIKE :id ")
    suspend fun updateBookmarks(bookmarkState:String,id:Int):Int

    @Query("UPDATE userData SET likes = :likedPostsId WHERE id LIKE :userId ")
    suspend fun updateLikes(likedPostsId:String,userId:Int):Int
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSewMethods(recipes: List<SewEntity>): LongArray



   /* @Query("SELECT EXISTS(SELECT bookMars FROM userData WHERE id = :id)")
    fun isBookMarkedOrNot(id : Int) : Boolean*/
    /*@Query("SELECT id FROM bookMarkedSewMethods WHERE id = :id")
    suspend fun getBookMarkedSewById(id: Int): Int*/
    @Query("SELECT * FROM sewMethods WHERE id = :id")
    suspend fun getSewById(id: Int): SewEntity?

   /* @Query("DELETE FROM userData WHERE id IN (:ids)")
    suspend fun deleteSew(ids: List<Int>): Int*/

    @Query("DELETE FROM sewMethods")
    suspend fun deleteAllSewMethods()

    @Query("DELETE FROM sewMethods WHERE id = :primaryKey")
    suspend fun deleteSew(primaryKey: Int): Int

    /**
     * Retrieve recipes for a particular page.
     * Ex: page = 2 retrieves recipes from 30-60.
     * Ex: page = 3 retrieves recipes from 60-90
     */
    @Query("""
        SELECT * FROM sewMethods 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%'  
        ORDER BY date_updated DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """)
    suspend fun searchSewMethods(
        query: String,
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<SewEntity>

    /**
     * Same as 'searchRecipes' function, but no query.
     */
    @Query("""
        SELECT * FROM sewMethods 
        ORDER BY date_updated DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
    """)
    suspend fun getAllSewMethods(
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<SewEntity>

    /**
     * Restore Recipes after process death
     */
    @Query("""
        SELECT * FROM sewMethods 
        WHERE title LIKE '%' || :query || '%'
        OR description LIKE '%' || :query || '%' 
        ORDER BY date_updated DESC LIMIT (:page * :pageSize)
        """)
    suspend fun restoreSewMethods(
        query: String,
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<SewEntity>

    /**
     * Same as 'restoreRecipes' function, but no query.
     */
    @Query("""
        SELECT * FROM sewMethods 
        ORDER BY date_updated DESC LIMIT (:page * :pageSize)
    """)
    suspend fun restoreAllSewMethods(
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<SewEntity>
}

