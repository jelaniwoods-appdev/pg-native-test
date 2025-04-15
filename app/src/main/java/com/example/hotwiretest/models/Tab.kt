package com.example.hotwiretest.models

import androidx.annotation.IdRes
import com.example.hotwiretest.R

data class Tab(
    val name: String,
    val path: String,
    val available: Boolean,
    val authenticated: Boolean,
    @IdRes val menuId: Int,
    @IdRes val navigatorHostId: Int
) {
    companion object {
        val all = arrayOf(
            Tab(
                name = "feed",
                path = "",
                menuId = R.id.bottom_nav_feed,
                available = false,
                authenticated = true,
                navigatorHostId = R.id.feed_nav_host
            ),
            Tab(
                name = "discover",
                path = "discover",
                menuId = R.id.bottom_nav_discover,
                available = false,
                authenticated = true,
                navigatorHostId = R.id.discover_nav_host
            ),
            Tab(
                name = "search",
                path = "users",
                menuId = R.id.bottom_nav_search,
                available = false,
                authenticated = true,
                navigatorHostId = R.id.search_nav_host
            ),
            Tab(
                name = "your profile",
                path = "profile",
                menuId = R.id.bottom_nav_profile,
                available = false,
                authenticated = true,
                navigatorHostId = R.id.edit_profile_nav_host
            ),
            Tab(
                name = "sign in",
                path = "users/sign_in",
                menuId = R.id.bottom_nav_sign_in,
                available = true,
                authenticated = false,
                navigatorHostId = R.id.sign_in_nav_host
            )
        )
        val default = Tab(
            name = "sign in",
            path = "users/sign_in",
            menuId = R.id.bottom_nav_feed,
            available = true,
            authenticated = true,
            navigatorHostId = R.id.sign_in_nav_host
        )
        val other = arrayOf(
            Tab(
                name = "feed",
                path = "users/sign_in",
                menuId = R.id.bottom_nav_feed,
                available = true,
                authenticated = true,
                navigatorHostId = R.id.feed_nav_host
            )
        )
    }
}
