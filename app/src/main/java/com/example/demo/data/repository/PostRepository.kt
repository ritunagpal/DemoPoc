package com.example.demo.data.repository

import com.example.demo.data.local.PostDao
import com.example.demo.data.model.Post
import com.example.demo.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao
) {
    fun getPosts(): Flow<List<Post>> {
        return postDao.getAllPosts()
    }

    fun getFavoritePosts(): Flow<List<Post>> {
        return postDao.getFavoritePosts()
    }

    suspend fun refreshPosts() {
        try {
            val posts = apiService.getPosts()
            val currentPosts = postDao.getAllPosts().first()
            val currentFavorites = currentPosts.filter { it.isFavorite }.map { it.id }.toSet()

            val postsWithFavorites = posts.map { post ->
                post.copy(isFavorite = currentFavorites.contains(post.id))
            }

            postDao.deleteAllPosts()
            postDao.insertPosts(postsWithFavorites)
        } catch (e: Exception) {
            // Network error - use cached data
        }
    }

    suspend fun toggleFavorite(post: Post) {
        val updatedPost = post.copy(isFavorite = !post.isFavorite)
        postDao.updatePost(updatedPost)
    }

    suspend fun deleteFromFavorites(postId: Int) {
        val posts = postDao.getAllPosts().first()
        val post = posts.find { it.id == postId }
        post?.let {
            val updatedPost = it.copy(isFavorite = false)
            postDao.updatePost(updatedPost)
        }
    }
}
