package com.example.hotwiretest.activities

import android.os.Bundle
import android.util.Log
import com.example.hotwiretest.R
import android.view.View
import android.webkit.CookieManager
import androidx.navigation.findNavController
import dev.hotwire.navigation.activities.HotwireActivity
import com.example.hotwiretest.models.Tab
import dev.hotwire.navigation.navigator.NavigatorConfiguration
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.hotwire.navigation.navigator.Navigator
import dev.hotwire.navigation.navigator.NavigatorHost

// manipulate fragment state
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentManager

const val baseURL = "http://10.0.2.2:3000"
//const val baseURL = "https://photogram-native.matchthetarget.com"

class MainActivity : HotwireActivity() {
    private val tabs = Tab.all//.filter { !it.available }
//    private var other = Tab.all.filter { it.name.contains("profile") }
//    private var other = Tab.all.filter { it.available }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { tab ->
//            val selectedTab = tabs.first { it.menuId == tab.itemId }
            val selectedTab = Tab.default
            showTab(selectedTab)
            true
        }

//        Log.d("Cookies", "ORM is -> ${tabs.javaClass}");

        showTab(tabs.first())
        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                remoteFileUrl = "$baseURL/configurations/android_v1.json"
            )
        )
//        Log.d("Cookies", "tabs -> ${tabs.filter<Tab> { it.available }.javaClass}");
//        Log.d("Cookies", "tabs -> ${Tab.all.javaClass}");
//        Log.d("Cookies", "NEW has -> ${CookieManager.getInstance().hasCookies()}");
    }

    override fun navigatorConfigurations() = tabs.map { tab ->
        NavigatorConfiguration(
            name = tab.name,
            startLocation = "$baseURL/${tab.path}",
            navigatorHostId = tab.navigatorHostId
        )
    }

    private fun showTab(tab: Tab) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
//        Log.d("Cookies", "get -> ${CookieManager.getInstance().getCookie(baseURL)}");
        // remember_user_token presence indicates "signed in"
        var hasRememberMeToken = false
        if (CookieManager.getInstance().getCookie(baseURL) != null)
            hasRememberMeToken = CookieManager.getInstance().getCookie(baseURL).contains("remember_user_token", ignoreCase = true)
//        Log.d("Cookies", "token -> ${hasRememberMeToken}");
        tabs.forEach {
            val view = findViewById<View>(it.navigatorHostId)
//            Log.d("Cookies", "title -> ${view.accessibilityPaneTitle}");
//          // if view is already visible and it the tab is clicked, invalidate to reload?
//            view.visibility = if (it == tab) View.VISIBLE else View.GONE
            if (it == tab)
                view.visibility = View.VISIBLE
            else
                view.visibility = View.GONE
                if (hasRememberMeToken == true) {
//                    bottomNav.requestLayout()
//                    bottomNav.invalidate()

//                    unable to get navigator
//                    if (findNavController(bottomNav.id) != null) {
//                        val navC = findNavController(bottomNav.id)
//                        navC.currentDestination
//                        Log.d("Cookies", "nav dest -> ${navC.currentDestination}")
//                    }


//                    view.postInvalidate()
//                    Log.d("Cookies", "invalidating view ->")
                    Log.d("Cookies", "signed in")
                }

        }
    }
}