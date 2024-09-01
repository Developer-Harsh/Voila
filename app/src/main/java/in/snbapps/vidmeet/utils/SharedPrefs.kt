package `in`.snbapps.vidmeet.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import `in`.snbapps.vidmeet.data.User

class SharedPrefs(context: Context) {

    private val prefsName = "Voila"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val SECRET_KEY = "secretKey"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER = "user"
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        sharedPreferences.edit().putString(KEY_USER, userJson).apply()
    }

    fun updateUserField(field: (User) -> Unit) {
        val user = getUser()
        if (user != null) {
            field(user)
            saveUser(user)
        }
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun saveSecretKey(key: String) {
        sharedPreferences.edit().putString(SECRET_KEY, key).apply()
    }

    fun fetchSecretKey(): String? {
        return sharedPreferences.getString(SECRET_KEY, "")
    }

    fun saveLoggedInState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}
