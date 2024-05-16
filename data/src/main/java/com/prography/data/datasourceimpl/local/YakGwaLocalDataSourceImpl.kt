package com.prography.data.datasourceimpl.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.prography.data.datasource.local.YakGwaLocalDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YakGwaLocalDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context
) : YakGwaLocalDataSource {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = FILE_NAME)

    private val dataStore = context.dataStore

    override val accessToken: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[ACCESS_TOKEN_KEY] ?: ""
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
            preferences[REFRESH_TOKEN_KEY] ?: ""
        }

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = accessToken
        }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private const val FILE_NAME = "YakGwa"
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("REFRESH_TOKEN")
    }
}