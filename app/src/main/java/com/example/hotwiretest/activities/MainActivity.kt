package com.example.hotwiretest.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.hotwiretest.R
import android.view.View
import android.webkit.CookieManager
import androidx.core.view.children
import androidx.navigation.findNavController
import dev.hotwire.navigation.activities.HotwireActivity
import com.example.hotwiretest.models.Tab
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.navigation.activities.HotwireActivityDelegate
import dev.hotwire.navigation.fragments.HotwireFragment
import dev.hotwire.navigation.navigator.NavigatorHost

const val baseURL = "http://10.0.2.2:3000"
//const val baseURL = "https://photogram-native.matchthetarget.com"

class MainActivity : HotwireActivity() {
    private val tabs = Tab.all

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("Cookies", "->creating")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { tab ->
            val selectedTab = tabs.first { it.menuId == tab.itemId }
            showTab(selectedTab)
            var nh = delegate.navigatorHost(selectedTab.navigatorHostId)
            Log.i("Cookies", "->showing tab, get path ${nh.navigator.location}")
            if (!nh.navigator.location?.contains("users/sign_in")!!) {
                Log.i("Cookies", "-> HIDE? ${selectedTab.path}")
                bottomNav.menu.findItem(R.id.bottom_nav_sign_in).setVisible(false)
//                bottomNav.menu.removeItem(R.id.bottom_nav_sign_in)
            } else {
                Log.i("Cookies", "-> signed OUT make visible")
//                delegate.resetNavigators()
//                bottomNav.menu.children.indexOf()
//                bottomNav.menu.findItem(bottomNav.menu.size() - 1).setVisible(true)
                bottomNav.menu.findItem(R.id.bottom_nav_sign_in).setVisible(true)
//                bottomNav.menu.findItem(R.id.bottom_nav_sign_in)
//                navigatorConfigurations()

            }
            true
        }

        Log.i("Cookies", "pre-show tab")
        showTab(Tab.default)
        Log.i("Cookies", "-> loading config")
        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "$baseURL/configurations/android_v1.json"
            )
        )
    }

    override fun navigatorConfigurations() = tabs.map { tab ->
        Log.i("Cookies", "->navcnongi")
        NavigatorConfiguration(
            name = tab.name,
            startLocation = "$baseURL/${tab.path}",
            navigatorHostId = tab.navigatorHostId
        )
    }


    private fun showTab(tab: Tab) {
        var x = delegate.currentNavigator?.host?.id?.toString()
        if (x != null) {
            Log.i("Cookies", x)
        } else {
            Log.i("Cookies", "nah")
        }
        var signedIn = CookieManager.getInstance().getCookie(baseURL).contains("remember_user_token")
        Log.i("Cookies", "signed in -> " + signedIn.toString())
        if (!signedIn) {
            Log.i("Cookies", "showing sign in only? -> " + signedIn.toString())
//            delegate.resetNavigators()

//            delegate.setCurrentNavigator(
//                NavigatorConfiguration(
//                    name = "sign in",
//                    startLocation = "users/sign_n",
//                    navigatorHostId = R.id.sign_in_nav_host
//                )
//            )
        }
        tabs.forEach {
            val view = findViewById<View>(it.navigatorHostId)
            view.visibility = if (it == tab) View.VISIBLE else View.GONE
        }
    }

    private fun isSignedIn() {
        var x = CookieManager.getInstance().getCookie(baseURL)
        Log.i("Cookies", x)
    }
}