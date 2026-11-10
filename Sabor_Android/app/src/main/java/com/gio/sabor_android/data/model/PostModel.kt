package com.gio.sabor_android.data.model

data class PostModel(
    val id: String = "",
    val userId: String = "",
    val caption: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0L,
    val likesCount: Int = 0,
    val likedByUsers: Map<String, Boolean> = emptyMap()
)