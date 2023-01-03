package dev.diegodc.chefio.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.diegodc.chefio.data.repositories.IReceiptsRepository
import dev.diegodc.chefio.data.sources.receipt.ReceiptDataSource
import dev.diegodc.chefio.data.sources.receipt.ReceiptRepository
import dev.diegodc.chefio.data.sources.receipt.remote.ReceiptRemoteDataSource
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
    fun provideReceiptRepository(
        @RemoteDataSource remoteDataSource: ReceiptDataSource,
    ): IReceiptsRepository {
        return ReceiptRepository(remoteDataSource= remoteDataSource,)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @RemoteDataSource
    @Provides
    fun provideRemoteDataSource(firestore: FirebaseFirestore) : ReceiptDataSource{
        return ReceiptRemoteDataSource(firestore)
    }

    @Singleton
    @Provides
    fun provideFireStore() :FirebaseFirestore = Firebase.firestore
}

