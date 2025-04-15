package ru.prod.application.ai.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.prod.application.ai.data.repositories.AIChatRepository
import ru.prod.application.ai.data.repositories.AiChatRepositoryImpl
import ru.prod.application.ai.data.source.network.aiApiService.AIApiService
import ru.prod.application.auth.AuthManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AIModule {
    @Provides
    @Singleton
    fun provideAIApiService(httpClient: HttpClient): AIApiService {
        return AIApiService(httpClient)
    }
    @Provides
    @Singleton
    fun provideAIRepository(aiApiService: AIApiService, authManager: AuthManager): AIChatRepository {
        return AiChatRepositoryImpl(authManager, aiApiService)
    }
}