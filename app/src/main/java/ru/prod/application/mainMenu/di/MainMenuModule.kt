package ru.prod.application.mainMenu.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ru.prod.application.auth.AuthManager
import ru.prod.application.mainMenu.data.repository.profileRepository.ProfileRepository
import ru.prod.application.mainMenu.data.sorce.network.availibleLocations.AvailableLocationsApiService
import ru.prod.application.mainMenu.data.sorce.network.profileApiService.ProfileApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainMenuModule {
    @Provides
    @Singleton
    fun provideProfileApiService(): ProfileApiService {
        return ProfileApiService()
    }

    @Provides
    @Singleton
        fun provideProfileRepository(
        profileApiService: ProfileApiService,
        authManager: AuthManager,
        httpClient: HttpClient,
        ): ProfileRepository {
        return ProfileRepository(profileApiService, authManager, httpClient)
    }

    @Provides
    @Singleton
    fun provideAvailableLocationsApiService(httpClient: HttpClient, authManager: AuthManager): AvailableLocationsApiService {
        return AvailableLocationsApiService(httpClient, authManager)
    }

}