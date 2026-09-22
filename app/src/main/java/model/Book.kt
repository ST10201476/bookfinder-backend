package com.example.bookfinder.model

data class BookResponse(
    val totalItems: Int,
    val items: List<BookItem>?
)

data class BookItem(
    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val publisher: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val averageRating: Double?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(
    val thumbnail: String?,
    val smallThumbnail: String?
)

