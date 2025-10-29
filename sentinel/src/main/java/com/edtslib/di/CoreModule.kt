package com.edtslib.di

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.edtslib.Sentinel
import com.google.gson.Gson
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

val networkingModule = module {
    single(named("sentinelOkHttp")) { provideOkHttpClient() }
    single { provideGson() }
    single { provideGsonConverterFactory(get()) }

    single(named("sentinel")) { provideRetrofit(get(named("sentinelOkHttp")), get()) }
}

val sharedPreferencesModule = module {
    single(named("sentinelSharePref")) {
        try {
            if (Sentinel.debugging) {
                PreferenceManager.getDefaultSharedPreferences(androidContext())
            }
            else {
                val spec = KeyGenParameterSpec.Builder(
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
                    .build()
                val masterKey = MasterKey.Builder(androidContext())
                    .setKeyGenParameterSpec(spec)
                    .build()

                EncryptedSharedPreferences.create(
                    androidContext(),
                    "edts_sentinel_secret_shared_prefs",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
                }
        }

        catch (_: Exception) {
            PreferenceManager.getDefaultSharedPreferences(androidContext())
        }
        catch (_: NoClassDefFoundError) {
            PreferenceManager.getDefaultSharedPreferences(androidContext())
        }
    }
}

private fun provideOkHttpClient(): OkHttpClient = UnsafeOkHttpClient().get()

private fun provideGson(): Gson = Gson()

private fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
    GsonConverterFactory.create(gson)

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Sentinel.baseUrl)
        .client(okHttpClient.newBuilder().addInterceptor(AuthInterceptor(Sentinel.apiKey)).build())
        .addConverterFactory(converterFactory)
        .build()
}