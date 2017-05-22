package com.heed.justquotes.utils

import android.app.Application
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Howard.
 */
class AppController : Application() {

    private val TAG: String = this@AppController.javaClass.simpleName

    override fun onCreate() {
        super.onCreate()

        // SDK inits
        Realm.init(this@AppController)

        val config: RealmConfiguration = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
    }

    fun clearRealmDb(config: RealmConfiguration) {
        try {
            Realm.deleteRealm(config)
        } catch (e: Exception) { // No realm file to remove
            Log.e(TAG, e.message, e)
        }
    }
}