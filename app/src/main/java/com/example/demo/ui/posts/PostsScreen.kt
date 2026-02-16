package com.example.demo.ui.posts

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.demo.ui.posts.components.FavoritesTabContent
import com.example.demo.ui.posts.components.PostsTabContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(
    viewModel: PostsViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Posts") },
                actions = {
                    IconButton(onClick = { viewModel.logout(onLogout) }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Posts") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Favorites") }
                )
            }

            when (selectedTabIndex) {
                0 -> PostsTabContent(
                    posts = uiState.posts,
                    isLoading = uiState.isLoading,
                    onRefresh = viewModel::refreshPosts,
                    onToggleFavorite = viewModel::toggleFavorite
                )
                1 -> FavoritesTabContent(
                    favoritePosts = uiState.favoritePosts,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onDeleteFromFavorites = viewModel::deleteFromFavorites
                )
            }
        }
    }
}
