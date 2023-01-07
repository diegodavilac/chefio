package dev.diegodc.chefio.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.diegodc.chefio.data.repositories.IAuthRepository
import dev.diegodc.chefio.data.repositories.IRecipesRepository
import dev.diegodc.chefio.data.sources.auth.AuthDataSource
import dev.diegodc.chefio.data.sources.auth.AuthRepository
import dev.diegodc.chefio.data.sources.auth.firebase.AuthFirebaseDataSource
import dev.diegodc.chefio.data.sources.recipe.RecipeDataSource
import dev.diegodc.chefio.data.sources.recipe.RecipeRepository
import dev.diegodc.chefio.data.sources.recipe.remote.RecipePagingSource
import dev.diegodc.chefio.data.sources.recipe.remote.RecipeRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteDataSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalDataSource

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        @RemoteDataSource remoteDataSource: RecipeDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): IRecipesRepository {
        return RecipeRepository(remoteDataSource= remoteDataSource, dispatcher = dispatcher )
    }

    @Singleton
    @Provides
    fun provideAuthRepository (
        @RemoteDataSource remoteDataSource: AuthDataSource,
    ): IAuthRepository {
        return AuthRepository(remoteDataSource = remoteDataSource)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideRecipePagingSource (
        @RemoteDataSource remoteDataSource: RecipeDataSource
    ) : RecipePagingSource{
        return RecipePagingSource(remoteDataSource)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideAuthRemoteDataSource(firebaseAuth: FirebaseAuth) : AuthDataSource = AuthFirebaseDataSource(auth = firebaseAuth)

    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(firestore: FirebaseFirestore, @ApplicationContext appContext: Context) : RecipeDataSource{
        return RecipeRemoteDataSource(firestore, appContext)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth() :FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFireStore() :FirebaseFirestore = Firebase.firestore
}

