package com.simplemobiletools.gallery.pro.ui.activities

import android.content.Intent
import com.simplemobiletools.commons.activities.BaseSplashActivity
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.gallery.pro.data.extensions.context.config
import com.simplemobiletools.gallery.pro.data.extensions.context.favoritesDB
import com.simplemobiletools.gallery.pro.data.extensions.context.getFavoriteFromPath
import com.simplemobiletools.gallery.pro.data.extensions.context.mediaDB
import com.simplemobiletools.gallery.pro.data.models.Favorite

class SplashActivity : BaseSplashActivity() {
    override fun initActivity() {

        // check if previously selected favorite items have been properly migrated into the new Favorites table
        if (config.wereFavoritesMigrated) {
            launchActivity()
        } else {
            if (config.appRunCount == 0) {
                config.wereFavoritesMigrated = true
                launchActivity()
            } else {
                config.wereFavoritesMigrated = true
                ensureBackgroundThread {
                    val favorites = ArrayList<Favorite>()
                    val favoritePaths = mediaDB.getFavorites().map { it.path }.toMutableList() as ArrayList<String>
                    favoritePaths.forEach {
                        favorites.add(getFavoriteFromPath(it))
                    }
                    favoritesDB.insertAll(favorites)

                    runOnUiThread {
                        launchActivity()
                    }
                }
            }
        }
    }

    private fun launchActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
