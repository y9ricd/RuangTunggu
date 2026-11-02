package com.ruangtunggu.data.remote

import com.ruangtunggu.BuildConfig
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage

object SupabaseClient {

    val client by lazy {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL.ifBlank { "https://yqfoolhywaxntsmhvqsv.supabase.co" },
            supabaseKey = BuildConfig.SUPABASE_ANON_KEY.ifBlank { "placeholder_anon_key" }
        ) {
            install(Auth)
            install(Postgrest)
            install(Realtime)
            install(Storage)
        }
    }

    val auth get() = client.auth
    val postgrest get() = client.postgrest
    val realtime get() = client.realtime
    val storage get() = client.storage
}
