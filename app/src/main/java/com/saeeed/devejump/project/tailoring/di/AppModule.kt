package com.saeeed.devejump.project.tailoring.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.saeeed.devejump.project.tailoring.BaseApplication
import com.saeeed.devejump.project.tailoring.cash.SewMethodDao
import com.saeeed.devejump.project.tailoring.cash.database.AppDatabase
import com.saeeed.devejump.project.tailoring.cash.model.CommentEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.FollowersEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.FollowingsEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.PostEntityMapper
import com.saeeed.devejump.project.tailoring.cash.model.UserDataEntityMapper
import com.saeeed.devejump.project.tailoring.interactor.register.RegisterUser
import com.saeeed.devejump.project.tailoring.interactor.Splash.GetUserStuffsFromServer
import com.saeeed.devejump.project.tailoring.interactor.description.GetComments
import com.saeeed.devejump.project.tailoring.interactor.description.GetSewMethod
import com.saeeed.devejump.project.tailoring.interactor.description.UserActivityOnPost
import com.saeeed.devejump.project.tailoring.interactor.followers.GetFollowersList
import com.saeeed.devejump.project.tailoring.interactor.followings.GetFollowingsList
import com.saeeed.devejump.project.tailoring.interactor.home.Bests
import com.saeeed.devejump.project.tailoring.interactor.home.GetHomeData
import com.saeeed.devejump.project.tailoring.interactor.notifications.GetNotifications
import com.saeeed.devejump.project.tailoring.interactor.school.GetSchoolData
import com.saeeed.devejump.project.tailoring.interactor.sew_list.RestoreSewMethods
import com.saeeed.devejump.project.tailoring.interactor.sew_list.SearchSew
import com.saeeed.devejump.project.tailoring.interactor.upload_post.UploadPostFunctions
import com.saeeed.devejump.project.tailoring.interactor.user_profile.GetUserProfileData
import com.saeeed.devejump.project.tailoring.network.RetrofitService
import com.saeeed.devejump.project.tailoring.network.model.ArticleDtoMapper
import com.saeeed.devejump.project.tailoring.network.model.BannerMapper
import com.saeeed.devejump.project.tailoring.network.model.CommentMapper
import com.saeeed.devejump.project.tailoring.network.model.NotificationMapper
import com.saeeed.devejump.project.tailoring.network.model.PeoplesMapper
import com.saeeed.devejump.project.tailoring.network.model.PostMapper
import com.saeeed.devejump.project.tailoring.network.model.ProductDtoMapper
import com.saeeed.devejump.project.tailoring.network.model.UserDataMapper
import com.saeeed.devejump.project.tailoring.repository.SewRepository
import com.saeeed.devejump.project.tailoring.repository.SewRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val USER_Data = "user_data"

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideMapper():PostMapper {
        return PostMapper()
    }

    @Singleton
    @Provides
    fun provideProductDtoMapper():ProductDtoMapper {
        return ProductDtoMapper()
    }

    @Singleton
    @Provides
    fun provideUploadPostFunctions(): UploadPostFunctions {
        return UploadPostFunctions()
    }

    @Singleton
    @Provides
    fun provideFollowersMapper(): PeoplesMapper {
        return PeoplesMapper()
    }

    @Singleton
    @Provides
    fun provideUserDataMapperMapper(): UserDataMapper {
        return UserDataMapper()
    }

    @Singleton
    @Provides
    fun provideBannerMapper(): BannerMapper {
        return BannerMapper()
    }

    @Singleton
    @Provides
    fun provideCommentMapper(): CommentMapper {
        return CommentMapper()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(context,USER_Data)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(USER_Data) }
        )
    }

   /* @Singleton
    @Provides
    @Named("author_id")
    suspend fun getUserFromPreferencesStore(userPreferencesDataStore:DataStore<Preferences>): Int? {
        val dataStoreKey= intPreferencesKey("user_id")
        val preferences=userPreferencesDataStore.data.first()
       return preferences[dataStoreKey]
    }*/

    @Singleton
    @Provides
    fun provideRetrofitService(
        client: OkHttpClient
    ): RetrofitService {
        return Retrofit.Builder()
            //.baseUrl("https://dev-xf7awpzkvndkoch.api.raw-labs.com/")
         //  .baseUrl("https://food2fork.ca/api/recipe/")
            .baseUrl("http://10.0.2.2:5198/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
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
        postMapper: PostMapper,
    ): SewRepository{
        return SewRepositoryImpl(
            recipeService = retrofitService,
            mapper = postMapper
        )
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .allowMainThreadQueries()
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
    fun provideCacheRecipeMapper(): PostEntityMapper {
        return PostEntityMapper()
    }


    @Singleton
    @Provides
    fun provideCommentEntityMapper(): CommentEntityMapper {
        return CommentEntityMapper()
    }


    @Singleton
    @Provides
    fun provideUserDataMapper(): UserDataEntityMapper {
        return UserDataEntityMapper()
    }

    @Singleton
    @Provides
    fun provideFollowersEntityMapper(): FollowersEntityMapper {
        return FollowersEntityMapper()
    }
    @Singleton
    @Provides
    fun provideFollowingsEntityMapper(): FollowingsEntityMapper {
        return FollowingsEntityMapper()
    }

    @Singleton
    @Provides
    fun provideSearchRecipe(
        retrofitService: RetrofitService,
        sewMethodDao: SewMethodDao,
        postEntityMapper: PostEntityMapper,
        postDtoMapper: PostMapper,
    ): SearchSew {
        return SearchSew(
            retrofitService = retrofitService,
            sewMethodDao = sewMethodDao,
            entityMapper = postEntityMapper,
            dtoMapper = postDtoMapper,
        )
    }

    @Singleton
    @Provides
    fun provideRegisterUser(
        retrofitService: RetrofitService,
        userDataMapper: UserDataMapper,
        sewMethodDao: SewMethodDao,
        userDataEntityMapper: UserDataEntityMapper,

        ): RegisterUser {
        return RegisterUser(
            retrofitService=retrofitService,
            userDataMapper=userDataMapper,
            sewMethodDao =sewMethodDao ,
            entityMapper = userDataEntityMapper

        )
    }

    @Singleton
    @Provides
    fun provideGetFollowersList(
        retrofitService: RetrofitService,
        sewMethodDao: SewMethodDao,
        followersEntityMapper: FollowersEntityMapper,
        dtoMapper: PeoplesMapper,
    ): GetFollowersList {
        return GetFollowersList(
            retrofitService = retrofitService,
            sewMethodDao = sewMethodDao,
            followersEntityMapper = followersEntityMapper,
            dtoMapper = dtoMapper,
        )
    }

    @Singleton
    @Provides
    fun provideGetFollowingsList(
        retrofitService: RetrofitService,
        sewMethodDao: SewMethodDao,
        followingsEntityMapper: FollowingsEntityMapper,
        dtoMapper: PeoplesMapper,
    ): GetFollowingsList {
        return GetFollowingsList(
            retrofitService = retrofitService,
            sewMethodDao = sewMethodDao,
            followingsEntityMapper = followingsEntityMapper,
            dtoMapper = dtoMapper,
        )
    }





    @Singleton
    @Provides
    fun provideGetHomeData(
        retrofitService: RetrofitService,
        bannerMapper: BannerMapper,
        dtoMapper: PostMapper
    ): GetHomeData {
        return GetHomeData(
            retrofitService = retrofitService,
            bannerMapper = bannerMapper,
            dtoMapper = dtoMapper

        )
    }

    @Singleton
    @Provides
    fun provideGetUserProfileData(
        retrofitService: RetrofitService,
        dtoMapper: PostMapper,
        entityMapper: PostEntityMapper,
        userDataEntityMapper: UserDataEntityMapper,
        sewMethodDao: SewMethodDao,
        userDataMapper: UserDataMapper

    ): GetUserProfileData {
        return GetUserProfileData(
            retrofitService = retrofitService,
            dtoMapper = dtoMapper,
            entityMapper = entityMapper,
            userDataEntityMapper = userDataEntityMapper,
            sewMethodDao = sewMethodDao,
            userDtoMapper =userDataMapper


        )
    }

    @Singleton
    @Provides
    fun provideBests(
        retrofitService: RetrofitService,
        sewDtoMapper: PostMapper,
    ): Bests {
        return Bests(
            retrofitService = retrofitService,
            dtoMapper = sewDtoMapper
        )
    }


    @Singleton
    @Provides
    fun provideRestoreRecipes(
        sewMethodDao: SewMethodDao,
        postEntityMapper: PostEntityMapper
    ): RestoreSewMethods {
        return RestoreSewMethods(
            sewMethodDao = sewMethodDao,
            entityMapper = postEntityMapper,
        )
    }

    @Singleton
    @Provides
    fun provideGetSewMethod(
        sewMethodDao: SewMethodDao,
        postEntityMapper: PostEntityMapper,
        retrofitService: RetrofitService,
        postDtoMapper: PostMapper,
        productDtoMapper: ProductDtoMapper
    ): GetSewMethod {
        return GetSewMethod(
            sewMethodDao= sewMethodDao,
            entityMapper = postEntityMapper,
            retrofitService= retrofitService,
            postMapper =  postDtoMapper,
            productDtoMapper=productDtoMapper


        )
    }
    @Singleton
    @Provides
    fun  provideArticleDtoMapper():ArticleDtoMapper{
        return ArticleDtoMapper()
    }



    @Singleton
    @Provides
    fun  provideNotificationDtoMapper():NotificationMapper{
        return NotificationMapper()
    }
    @Singleton
    @Provides
    fun provideGetNotificationsData(
        retrofitService: RetrofitService,
        dtoMapper: NotificationMapper,
    ): GetNotifications {
        return GetNotifications(
            retrofitService= retrofitService,
            dtoMapper = dtoMapper
        )
    }
    @Singleton
    @Provides
    fun provideGetSchoolData(
        retrofitService: RetrofitService,
        bannerMapper: BannerMapper,
        articleDtoMapper:ArticleDtoMapper

        ): GetSchoolData {
        return GetSchoolData(
            retrofitService= retrofitService,
            bannerMapper=bannerMapper,
            dtoMapper = articleDtoMapper
        )
    }

    @Singleton
    @Provides
    fun provideGetComments(
        sewMethodDao: SewMethodDao,
        retrofitService: RetrofitService,
        commentMapper: CommentMapper,
        commentEntityMapper: CommentEntityMapper
    ): GetComments {
        return GetComments(
            sewMethodDao=sewMethodDao,
            retrofitService= retrofitService,
            commentMapper = commentMapper,
            commentEntityMapper = commentEntityMapper
        )
    }

@Singleton
@Provides

fun provideUserData(
    sewMethodDao: SewMethodDao,
    userDataEntityMapper: UserDataEntityMapper,
    dtoMapper: UserDataMapper,
    retrofitService: RetrofitService
):GetUserStuffsFromServer {
    return GetUserStuffsFromServer(
        sewMethodDao = sewMethodDao,
        entityMapper = userDataEntityMapper,
        dtoMapper = dtoMapper,
        retrofitService = retrofitService
    )
}

    @Singleton
    @Provides

    fun provideUserActivityOnPost(
        sewMethodDao: SewMethodDao,
        userDataEntityMapper: UserDataEntityMapper,
        retrofitService: RetrofitService,
        postEntityMapper: PostEntityMapper,
        commentMapper: CommentMapper
    ):UserActivityOnPost {
        return UserActivityOnPost(
            sewMethodDao = sewMethodDao,
            entityMapper = userDataEntityMapper,
            retrofitService = retrofitService,
            postEntityMapper = postEntityMapper,
            commentDtoMapper = commentMapper
        )
    }


}