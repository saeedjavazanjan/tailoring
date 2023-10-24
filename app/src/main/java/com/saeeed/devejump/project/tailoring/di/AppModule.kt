package com.saeeed.devejump.project.tailoring.di

import android.content.Context
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.saeeed.devejump.project.tailoring.BaseApplication
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.database.AppDatabase
import com.saeeed.devejump.project.tailoring.cash.model.SewEntityMapper
import com.saeeed.devejump.project.tailoring.interactor.description.GetSewMethod
import com.saeeed.devejump.project.tailoring.interactor.home.BestOfMonth
import com.saeeed.devejump.project.tailoring.interactor.home.GetBanners
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.sew_list.SearchSew
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import com.saeeed.devejump.project.tailoring.network.model.SewMethodDto
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import com.saeeed.devejump.project.tailoring.repository.SewRepository
import com.saeeed.devejump.project.tailoring.repository.SewRepositoryImpl
import com.saeeed.devejump.project.tailoring.utils.ConnectivityManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideMapper(): SewMethodMapper {
        return SewMethodMapper()
    }

    @Singleton
    @Provides
    fun provideBannerMapper(): BannerMapper {
        return BannerMapper()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl("https://dev-xf7awpzkvndkoch.api.raw-labs.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RetrofitService::class.java)
    }


    @Singleton
    @Provides
    @Named("auth_token")
    fun provideAuthToken(): String{
        return "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
    }

    @Singleton
    @Provides
    fun provideRepository(
        retrofitService: RetrofitService,
        sewMethodMapper: SewMethodMapper,
    ): SewRepository{
        return SewRepositoryImpl(
            recipeService = retrofitService,
            mapper = sewMethodMapper
        )
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRecipeDao(db: AppDatabase): SewMethodDao{
        return db.recipeDao()
    }

    @Singleton
    @Provides
    fun provideCacheRecipeMapper(): SewEntityMapper {
        return SewEntityMapper()
    }

    @Singleton
    @Provides
    fun provideSearchRecipe(
        retrofitService: RetrofitService,
        sewMethodDao: SewMethodDao,
        sewEntityMapper: SewEntityMapper,
        sewDtoMapper: SewMethodMapper,
    ): SearchSew {
        return SearchSew(
            retrofitService = retrofitService,
            sewMethodDao = sewMethodDao,
            entityMapper = sewEntityMapper,
            dtoMapper = sewDtoMapper,
        )
    }
    @Singleton
    @Provides
    fun provideGetBanners(
        retrofitService: RetrofitService,
        bannerMapper: BannerMapper,
    ): GetBanners {
        return GetBanners(
            retrofitService = retrofitService,
            dtoMapper = bannerMapper
        )
    }

    @Singleton
    @Provides
    fun provideBestOfMonth(
        retrofitService: RetrofitService,
        sewDtoMapper: SewMethodMapper,
    ): BestOfMonth {
        return BestOfMonth(
            retrofitService = retrofitService,
            dtoMapper = sewDtoMapper
        )
    }


    @Singleton
    @Provides
    fun provideRestoreRecipes(
        sewMethodDao: SewMethodDao,
        sewEntityMapper: SewEntityMapper
    ): RestoreSewMethods {
        return RestoreSewMethods(
            sewMethodDao = sewMethodDao,
            entityMapper = sewEntityMapper,
        )
    }

    @Singleton
    @Provides
    fun provideGetRecipe(
        sewMethodDao: SewMethodDao,
        sewEntityMapper: SewEntityMapper,
        retrofitService: RetrofitService,
        sewDtoMapper: SewMethodMapper,
    ): GetSewMethod {
        return GetSewMethod(
            sewMethodDao= sewMethodDao,
            entityMapper = sewEntityMapper,
            retrofitService= retrofitService,
            sewMethodMapper =  sewDtoMapper,
        )
    }

}