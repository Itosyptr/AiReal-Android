package capstone.tim.aireal.data.pref


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    fun saveToken(token: String) {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences[TOKEN_KEY] = token
            }
        }
    }

    suspend fun getToken(): String {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY] ?: ""
        }.first()
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[USER_ID_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    fun saveUser(user: UserModel) {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences[NAME_KEY] = user.name
                preferences[EMAIL_KEY] = user.email
                preferences[USER_ID_KEY] = user.userId
                preferences[TOKEN_KEY] = user.token
                preferences[IS_LOGIN_KEY] = user.isLogin
            }
        }
    }

    fun login() {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences[IS_LOGIN_KEY] = true
            }
        }
    }

    fun logout() {
        applicationScope.launch {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    suspend fun isLoggedIn(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[IS_LOGIN_KEY] ?: false
        }.first()
    }



    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
