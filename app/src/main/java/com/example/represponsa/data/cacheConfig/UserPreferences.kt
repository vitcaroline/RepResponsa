package com.example.represponsa.data.cacheConfig

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.represponsa.data.model.User
import com.example.represponsa.presentation.ui.theme.RepublicTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object UserPreferences {
    private val Context.dataStore by preferencesDataStore(name = "user_prefs")

    private val KEY_UID = stringPreferencesKey("uid")
    private val KEY_FIRST_NAME = stringPreferencesKey("first_name")
    private val KEY_NICKNAME = stringPreferencesKey("nickName")
    private val KEY_EMAIL = stringPreferencesKey("email")
    private val KEY_PHONE = stringPreferencesKey("phone")
    private val KEY_ROLE = stringPreferencesKey("role")
    private val KEY_REPUBLIC_ID = stringPreferencesKey("republic_id")
    private val KEY_REPUBLIC_NAME = stringPreferencesKey("republic_name")
    private val KEY_REPUBLIC_THEME = stringPreferencesKey("republic_theme")


    suspend fun saveUser(context: Context, user: User) {
        context.dataStore.edit { prefs ->
            prefs[KEY_UID] = user.uid
            prefs[KEY_FIRST_NAME] = user.userName
            prefs[KEY_NICKNAME] = user.nickName
            prefs[KEY_EMAIL] = user.email
            prefs[KEY_PHONE] = user.phone
            prefs[KEY_ROLE] = user.role
            prefs[KEY_REPUBLIC_ID] = user.republicId
            prefs[KEY_REPUBLIC_NAME] = user.republicName
        }
    }

    suspend fun getUser(context: Context): User? {
        val prefs = context.dataStore.data.first()
        return if (prefs[KEY_UID].isNullOrBlank()) null else User(
            uid = prefs[KEY_UID] ?: "",
            userName = prefs[KEY_FIRST_NAME] ?: "",
            nickName = prefs[KEY_NICKNAME] ?: "",
            email = prefs[KEY_EMAIL] ?: "",
            phone = prefs[KEY_PHONE] ?: "",
            role = prefs[KEY_ROLE] ?: "",
            republicId = prefs[KEY_REPUBLIC_ID] ?: "",
            republicName = prefs[KEY_REPUBLIC_NAME] ?: ""
        )
    }

    suspend fun saveRepublicTheme(context: Context, theme: RepublicTheme) {
        context.dataStore.edit { prefs ->
            prefs[KEY_REPUBLIC_THEME] = theme.name
        }
    }

    val Context.republicThemeFlow: Flow<RepublicTheme>
        get() = dataStore.data.map { prefs ->
            val name = prefs[KEY_REPUBLIC_THEME] ?: RepublicTheme.AZUL.name
            RepublicTheme.valueOf(name)
        }

    suspend fun clearUserKeepTheme(context: Context) {
        val currentTheme = context.republicThemeFlow.first()
        context.dataStore.edit { prefs ->
            prefs.clear()
            prefs[KEY_REPUBLIC_THEME] = currentTheme.name
        }
    }
}
