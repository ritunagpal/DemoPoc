package com.example.demo.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Posts : Screen("posts")
}
