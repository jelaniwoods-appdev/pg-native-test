package com.example.hotwiretest.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.example.hotwiretest.R
import android.view.View
import android.webkit.CookieManager
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import dev.hotwire.navigation.activities.HotwireActivity
import com.example.hotwiretest.models.Tab
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.navigation.activities.HotwireActivityDelegate
import dev.hotwire.navigation.fragments.HotwireFragment
import dev.hotwire.navigation.navigator.NavigatorHost
import kotlin.math.sign

const val baseURL = "http://10.0.2.2:3000"
//const val baseURL = "https://photogram-native.matchthetarget.com"

class MainActivity : HotwireActivity() {
    private val tabs = Tab.all

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { tab ->
            val selectedTab = tabs.first { it.menuId == tab.itemId }
            showTab(selectedTab)
            true
        }

        if (isSignedIn()) {
            // signed in
            bottomNav.menu.findItem(R.id.bottom_nav_sign_in).setVisible(false)
            val hiddenTabs = tabs.filter { it.authenticated && !tabIsVisible(bottomNav, it) }
            hiddenTabs.forEach { bottomNav.menu.findItem(it.menuId).setVisible(true) }
            showTab(tabs.first())
        } else {
            // signed out
            bottomNav.menu.findItem(R.id.bottom_nav_sign_in).setVisible(true)
            showTab(tabs.last())
            val visibleTabs = tabs.filter { it.authenticated && tabIsVisible(bottomNav, it) }
            visibleTabs.forEach { bottomNav.menu.findItem(it.menuId).setVisible(false) }
        }

        tabs.forEach { setTabObserver(it) }
//        setTabObserver(tabs.get(3))
//        setTabObserver(tabs.last())

        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "$baseURL/configurations/android_v1.json"
            )
        )
    }

    override fun navigatorConfigurations() = tabs.map { tab ->
        NavigatorConfiguration(
            name = tab.name,
            startLocation = "$baseURL/${tab.path}",
            navigatorHostId = tab.navigatorHostId
        )
    }


    private fun showTab(tab: Tab) {
        tabs.forEach {
            val view = findViewById<View>(it.navigatorHostId)
            view.visibility = if (it == tab) View.VISIBLE else View.GONE
        }
    }

    private fun setTabObserver(tab: Tab) {
        Log.i("TabObserver", "tab -> ${tab.name}")
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment = supportFragmentManager.findFragmentById(tab.navigatorHostId) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { controller,destination,_ ->
            var signedIn = isSignedIn()
            var currentlyVisible = bottomNav.menu.findItem(tab.menuId).isVisible

            Log.i("TabObserver", "-> ${tab.name} -> ${delegate.currentNavigator?.location}")
            // when signing in
            if (signedIn && !tab.authenticated && currentlyVisible) {
                // make authenticated tabs visible
                // select feed tab
                // hide sign in tab
                val hiddenTabs = tabs.filter { it.authenticated && !tabIsVisible(bottomNav, it) }
                hiddenTabs.forEach { bottomNav.menu.findItem(it.menuId).setVisible(true) }
                showTab(tabs.first())
                bottomNav.menu.findItem(tabs.last().menuId).setVisible(false)

                Log.i("TabObserver", " hidden -> ${hiddenTabs.size}")
                if (hiddenTabs.size > 0)
                    delegate.resetNavigators()
                Log.i("TabObserver", "signing in -> ${tab.name}")
            // when signing out
            } else if (!signedIn && tab.authenticated && currentlyVisible) {
                // make sign in tab visible
                // select sign in tab
                // hide authenticated tabs
                bottomNav.menu.findItem(tabs.last().menuId).setVisible(true)
                showTab(tabs.last())
                val visibleTabs = tabs.filter { it.authenticated && tabIsVisible(bottomNav, it) }
                visibleTabs.forEach { bottomNav.menu.findItem(it.menuId).setVisible(false) }
                Log.i("TabObserver", " visible -> ${visibleTabs.size}")
                if (visibleTabs.size > 0)
                    delegate.resetNavigators()
                Log.i("TabObserver", "signing out -> ${tab.name}")
            }
        }
    }

    private fun isSignedIn() : Boolean{
        return CookieManager.getInstance().getCookie(baseURL).contains("remember_user_token")
    }

    private fun tabIsVisible(nav: BottomNavigationView, tab: Tab) : Boolean  {
        return nav.menu.findItem(tab.menuId).isVisible
    }
}