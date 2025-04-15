package ru.prod.application.core.data.source.local.sharedPrefSample

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Named

// Пример реализации работы с SharedPreferences
class SharedPreferencesSample @Inject constructor(
    @Named("shared_prefs") private val sharedPreferences: SharedPreferences
){
    companion object {
        const val CELL_NAME = "something"
    }

    fun getSomething(): String? {
        return sharedPreferences.getString(CELL_NAME, null)
    }

    fun setSomething(newValue: String) {
        sharedPreferences.edit().apply {
            putString(CELL_NAME, newValue)
            apply()
        }
    }
}