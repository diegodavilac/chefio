package dev.diegodc.chefio.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RemoteDataSource

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalDataSource