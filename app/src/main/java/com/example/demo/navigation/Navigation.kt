package com.example.demo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demo.data.repository.AuthRepository
import com.example.demo.ui.login.LoginScreen
import com.example.demo.ui.posts.PostsScreen

@Composable
fun AppNavigation(
    authRepository: AuthRepository
) {
    val navController = rememberNavController()
    val isLoggedIn by authRepository.isLoggedIn().collectAsStateWithLifecycle(initialValue = false)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Screen.Posts.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Posts.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
                LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Posts.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Posts.route) {
            PostsScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
