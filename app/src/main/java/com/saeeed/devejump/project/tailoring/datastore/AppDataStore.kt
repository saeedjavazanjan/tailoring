package com.saeeed.devejump.project.tailoring.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore (context: Context){
  //  private val settingDataStore: DataStore<Preferences> = app.createDataStore(name = "settings")
    private val Context.settingDataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user"
    )
    companion object{
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
    }
    private val scope = CoroutineScope(Main)

    init {
        observeDataStore(context)
    }

    val isDark = mutableStateOf(false)

    fun toggleTheme(context: Context){
        scope.launch {
           context.settingDataStore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY]?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    private  fun observeDataStore(context: Context){
        context.settingDataStore.data.onEach { preferences ->
            preferences[DARK_THEME_KEY]?.let { isDarkTheme ->
                isDark.value = isDarkTheme
            }
        }.launchIn(scope)
    }


}