package com.example.hotwiretest

import android.app.Application
import com.example.hotwiretest.activities.baseURL
import com.example.hotwiretest.components.ButtonComponent
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.registerBridgeComponents

class PhotogramNative : Application() {
    override fun onCreate() {
        super.onCreate()

        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "$baseURL/configurations/android_v1.json"
            )
        )

        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("button", ::ButtonComponent)
        )

        Hotwire.config.jsonConverter = KotlinXJsonConverter()
        Hotwire.config.applicationUserAgentPrefix = "Photogram Native;"
    }
}