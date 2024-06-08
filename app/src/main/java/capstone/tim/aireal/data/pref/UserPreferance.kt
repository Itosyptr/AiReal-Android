package capstone.tim.aireal.data.pref

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    suspend fun getToken(): String {
        var token = ""
        dataStore.data.collect { preferences ->
            token = preferences[TOKEN_KEY] ?: ""
        }
        return token
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[USER_ID_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    fun getDataStore(): DataStore<Preferences> {
        return dataStore
    }


    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[USER_ID_KEY] = user.userId
            preferences[TOKEN_KEY] = user.token // Save the token here
            preferences[STATE_KEY] = user.isLogin
        }
    }

    // Hapus fungsi getUser() dengan tipe Flow<UserModel> karena sudah ada fungsi getUser() yang mengembalikan String.


    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear() // Hapus semua preferensi atau hanya token dan state jika diperlukan
        }
    }
    suspend fun isLoggedIn(): Boolean { // Function isLoggedIn() tanpa perubahan
        var isLoggedIn = false
        dataStore.data.collect { preferences ->
            isLoggedIn = preferences[STATE_KEY] ?: false
        }
        return isLoggedIn
    }



    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val TOKEN_KEY = stringPreferencesKey("token")
        internal val STATE_KEY = booleanPreferencesKey("state")  // Ubah menjadi internal

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
