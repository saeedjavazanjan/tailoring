package com.saeeed.devejump.project.tailoring.cash

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saeeed.devejump.project.tailoring.cash.model.SewEntity
import com.saeeed.devejump.project.tailoring.utils.RECIPE_PAGINATION_PAGE_SIZE

@Dao
interface SewMethodDao {
    @Insert
    suspend fun insertRecipe(recipe: SewEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(recipes: List<SewEntity>): LongArray

    @Query("SELECT * FROM sewMethods WHERE id = :id")
    suspend fun getRecipeById(id: Int): SewEntity?

    @Query("DELETE FROM sewMethods WHERE id IN (:ids)")
    suspend fun deleteRecipes(ids: List<Int>): Int

    @Query("DELETE FROM sewMethods")
    suspend fun deleteAllRecipes()

    @Query("DELETE FROM sewMethods WHERE id = :primaryKey")
    suspend fun deleteRecipe(primaryKey: Int): Int

    /**
     * Retrieve recipes for a particular page.
     * Ex: page = 2 retrieves recipes from 30-60.
     * Ex: page = 3 retrieves recipes from 60-90
     */
    @Query("""
        SELECT * FROM sewMethods 
        WHERE title LIKE '%' || :query || '%'
        OR ingredients LIKE '%' || :query || '%'  
        ORDER BY date_updated DESC LIMIT :pageSize OFFSET ((:page - 1) * :pageSize)
        """)
    suspend fun searchRecipes(
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
    suspend fun getAllRecipes(
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<SewEntity>

    /**
     * Restore Recipes after process death
     */
    @Query("""
        SELECT * FROM sewMethods 
        WHERE title LIKE '%' || :query || '%'
        OR ingredients LIKE '%' || :query || '%' 
        ORDER BY date_updated DESC LIMIT (:page * :pageSize)
        """)
    suspend fun restoreRecipes(
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
    suspend fun restoreAllRecipes(
        page: Int,
        pageSize: Int = RECIPE_PAGINATION_PAGE_SIZE
    ): List<SewEntity>
}
