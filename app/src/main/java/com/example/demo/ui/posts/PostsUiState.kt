package com.example.demo.ui.posts

import com.example.demo.data.model.Post

data class PostsUiState(
    val posts: List<Post> = emptyList(),
    val favoritePosts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
