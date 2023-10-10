package com.saeeed.devejump.project.tailoring

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication:Application() {
    val settingDataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

}