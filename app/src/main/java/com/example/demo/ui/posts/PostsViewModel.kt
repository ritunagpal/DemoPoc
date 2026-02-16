package com.example.demo.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo.data.model.Post
import com.example.demo.data.repository.AuthRepository
import com.example.demo.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val postRepository: PostRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PostsUiState())
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    init {
        loadPosts()
        loadFavoritePosts()
        refreshPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            postRepository.getPosts()
                .collect { posts ->
                    _uiState.update { it.copy(posts = posts) }
                }
        }
    }

    private fun loadFavoritePosts() {
        viewModelScope.launch {
            postRepository.getFavoritePosts()
                .collect { favoritePosts ->
                    _uiState.update { it.copy(favoritePosts = favoritePosts) }
                }
        }
    }

    fun refreshPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                postRepository.refreshPosts()
                _uiState.update { it.copy(isLoading = false, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to load posts"
                    )
                }
            }
        }
    }

    fun toggleFavorite(post: Post) {
        viewModelScope.launch {
            postRepository.toggleFavorite(post)
        }
    }

    fun deleteFromFavorites(postId: Int) {
        viewModelScope.launch {
            postRepository.deleteFromFavorites(postId)
        }
    }

    fun logout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLogoutComplete()
        }
    }
}
