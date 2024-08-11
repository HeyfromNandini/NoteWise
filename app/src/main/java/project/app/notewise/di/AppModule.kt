package project.app.notewise.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger
import kotlinx.coroutines.flow.first
import kotlinx.serialization.ExperimentalSerializationApi
import project.app.notewise.data.DatabaseObject
import project.app.notewise.data.login.SignInResponse
import project.app.notewise.data.repository.DatabaseRepo
import project.app.notewise.domain.Constants
import project.app.notewise.domain.datastore.UserDatastore
import project.app.notewise.domain.network.ApiService
import project.app.notewise.domain.network.ApiServiceImpl
import project.app.notewise.domain.network.safeApiCall
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json = kotlinx.serialization.json.Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 10000000000
            }
        }
    }

    @Provides
    @Singleton
    fun provideApiService(client: HttpClient): ApiService {
        return ApiServiceImpl(client = client)
    }

    @Provides
    @Singleton
    fun provideUserDatastore(@ApplicationContext context: Context): UserDatastore {
        return UserDatastore(context)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepo(@ApplicationContext context: Context): DatabaseRepo {
        val dB = DatabaseObject.getInstance(context)
        return DatabaseRepo(dB.userInfoDao())
    }

}