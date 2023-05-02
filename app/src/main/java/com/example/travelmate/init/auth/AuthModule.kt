package com.example.travelmate.init.auth

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.example.travelmate.data.repository.AuthRepositoryImpl
import com.example.travelmate.domain.repository.AuthRepository

@Module
@InstallIn(ViewModelComponent::class)
class AuthModule {
    @Provides
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(
        auth = Firebase.auth
    )
}