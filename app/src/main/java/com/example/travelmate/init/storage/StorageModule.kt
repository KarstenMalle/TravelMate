package com.example.travelmate.init.storage

import com.example.travelmate.data.repository.PhotoRepositoryImpl
import com.example.travelmate.domain.repository.PhotoRepository
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    @Singleton
    fun providePhotoRepository(storage: FirebaseStorage): PhotoRepository {
        return PhotoRepositoryImpl(storage)
    }
}
