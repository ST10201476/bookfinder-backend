package com.example.bookfinder.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class SaveBookRequest(
    val userId: String,
    val bookId: String,
    val title: String,
    val author: String?
)

data class SaveBookResponse(
    val message: String?,
    val error: String?
)

data class SavedBooksResponse(
    val books: List<SavedBookItem>
)

data class SavedBookItem(
    val id: String,
    val bookId: String,
    val title: String,
    val author: String?
)

interface MyBackendApi {

    @POST("saveBook")
    suspend fun saveBook(@Body request: SaveBookRequest): Response<SaveBookResponse>

    @GET("getSavedBooks")
    suspend fun getSavedBooks(@Query("userId") userId: String): Response<SavedBooksResponse>
}