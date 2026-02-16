package com.example.demo.data.remote

import com.example.demo.data.model.Comment
import com.example.demo.data.model.Post
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("posts/{postId}/comments")
    suspend fun getComments(@Path("postId") postId: Int): List<Comment>
}
