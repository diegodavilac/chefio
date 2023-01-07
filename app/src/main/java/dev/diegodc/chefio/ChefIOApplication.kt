package dev.diegodc.chefio

import android.app.Application
import com.cloudinary.android.MediaManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChefIOApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val config = mapOf(
            "cloud_name"  to "dzj8irwze",
            "secure" to true
        )
        MediaManager.init(this, config)
    }
}