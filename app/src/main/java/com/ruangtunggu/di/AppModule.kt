package com.ruangtunggu.di

import com.ruangtunggu.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL.ifBlank { "https://yqfoolhywaxntsmhvqsv.supabase.co" },
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY.ifBlank { "placeholder_anon_key" }
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideAuth(client: SupabaseClient): Auth {
        return client.auth
    }

    @Provides
    @Singleton
    fun providePostgrest(client: SupabaseClient): Postgrest {
        return client.postgrest
    }

    @Provides
    @Singleton
    fun provideRealtime(client: SupabaseClient): Realtime {
        return client.realtime
    }

    @Provides
    @Singleton
    fun provideStorage(client: SupabaseClient): Storage {
        return client.storage
    }
}
