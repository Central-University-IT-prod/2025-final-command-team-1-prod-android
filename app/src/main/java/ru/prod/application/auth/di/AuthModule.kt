package ru.prod.application.auth.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.prod.application.auth.AuthManager
import ru.prod.application.auth.data.repositories.authRepository.AuthRepository
import ru.prod.application.auth.data.repositories.authRepository.AuthRepositoryImpl
import ru.prod.application.auth.data.source.local.authSource.AuthTokenSource
import ru.prod.application.auth.data.source.network.authApi.AuthApiService
import ru.prod.application.data.ApiService
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    @Singleton
    fun provideAuthManager(authTokenSource: AuthTokenSource): AuthManager {
        return AuthManager(authTokenSource)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(httpClient: HttpClient): AuthApiService {
        return AuthApiService(httpClient)
    }

    @Provides
    @Singleton
    fun provideAuthTokenSource(
        @Named("encrypted_shared_prefs") encryptedSharedPreferences: SharedPreferences
    ): AuthTokenSource {
        return AuthTokenSource(encryptedSharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        authManager: AuthManager,
        apiService: ApiService
    ): AuthRepository {
        return AuthRepositoryImpl(authApiService, authManager, apiService)
    }
}