package com.example.travelmate.init.chat

import com.example.travelmate.data.repository.ChatRepositoryImpl
import com.example.travelmate.domain.repository.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChatModule {

    @Provides
    @Singleton
    fun provideChatRepository(firestore: FirebaseFirestore): ChatRepository {
        return ChatRepositoryImpl(firestore)
    }
}
