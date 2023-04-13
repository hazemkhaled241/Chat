package com.example.chat.di

import android.content.Context
import com.example.chat.data.local.CountryDataBase
import com.example.chat.data.local.dao.CountryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Singleton
    @Provides
    fun provideDao(@ApplicationContext context: Context):CountryDao{
        return CountryDataBase.getDataBase(context).countryDao()
    }
}