package com.dena.autum_hackathon_b.cassette.data.source

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var accessToken: String?
}

class SharedPreferenceStorage @Inject constructor(@ApplicationContext context: Context) :
    PreferenceStorage {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override var accessToken by StringPreference(prefs, PREF_ACCESS_TOKEN, null)

    companion object {
        private const val PREFS_NAME = "SPAJAM2021"
        private const val PREF_ACCESS_TOKEN = "prefAccessToken"
    }
}

class IntPreference(
    private val prefs: SharedPreferences,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return prefs.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        prefs.edit { putInt(name, value) }
    }
}

class BooleanPreference(
    private val prefs: SharedPreferences,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return prefs.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        prefs.edit { putBoolean(name, value) }
    }
}

class StringPreference(
    private val prefs: SharedPreferences,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return prefs.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        prefs.edit { putString(name, value) }
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceStorageModule() {
    @Binds
    abstract fun bindsPreferenceStorage(impl: SharedPreferenceStorage): PreferenceStorage
}
