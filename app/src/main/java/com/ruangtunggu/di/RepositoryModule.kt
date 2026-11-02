package com.ruangtunggu.di

import com.ruangtunggu.data.repository.AuthRepository
import com.ruangtunggu.data.repository.AuthRepositoryImpl
import com.ruangtunggu.data.repository.HospitalRepository
import com.ruangtunggu.data.repository.HospitalRepositoryImpl
import com.ruangtunggu.data.repository.QueueRepository
import com.ruangtunggu.data.repository.QueueRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindHospitalRepository(
        impl: HospitalRepositoryImpl
    ): HospitalRepository

    @Binds
    @Singleton
    abstract fun bindQueueRepository(
        impl: QueueRepositoryImpl
    ): QueueRepository
}

