package com.saeeed.devejump.project.tailoring.di

import com.google.gson.GsonBuilder
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.SewMethodMapper
import com.saeeed.devejump.project.tailoring.repository.SewRepository
import com.saeeed.devejump.project.tailoring.repository.SewRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
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
    fun provideMapper(): SewMethodMapper {
        return SewMethodMapper()
    }

    @Singleton
    @Provides
    fun provideRetrofitService(): RetrofitService {
        return Retrofit.Builder()
            .baseUrl("https://food2fork.ca/api/recipe/")
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
}