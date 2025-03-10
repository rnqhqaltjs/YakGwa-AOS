package com.yomo.data.datasourceimpl.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.yomo.data.datasource.local.YakGwaLocalDataSource
import decrypt
import encrypt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YakGwaLocalDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : YakGwaLocalDataSource {
    override val accessToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY]?.let { decrypt(it) } ?: ""
        }

    override val refreshToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[REFRESH_TOKEN_KEY]?.let { decrypt(it) } ?: ""
        }

    override val deviceToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[DEVICE_TOKEN_KEY]?.let { decrypt(it) } ?: ""
        }

    override val isLogin: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[IS_LOGIN_KEY] ?: false
        }

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = encrypt(accessToken)
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = encrypt(refreshToken)
        }
    }

    override suspend fun saveDeviceToken(deviceToken: String) {
        dataStore.edit { preferences ->
            preferences[DEVICE_TOKEN_KEY] = encrypt(deviceToken)
        }
    }

    override suspend fun saveIsLogin(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = isLogin
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
            preferences.remove(IS_LOGIN_KEY)
        }
    }

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
        private val DEVICE_TOKEN_KEY = stringPreferencesKey("DEVICE_TOKEN")
        private val IS_LOGIN_KEY = booleanPreferencesKey("IS_LOGIN")
    }
}