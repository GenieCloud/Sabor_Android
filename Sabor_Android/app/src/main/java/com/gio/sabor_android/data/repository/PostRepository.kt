package com.gio.sabor_android.data.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.gio.sabor_android.data.model.PostModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class PostRepository(private val context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun createPost(caption: String, imageUri: Uri): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not authenticated")
            val postId = UUID.randomUUID().toString()

            // 1. Convert image Uri to Base64 String (Bypasses Firebase Storage entirely!)
            val inputStream = context.contentResolver.openInputStream(imageUri)
                ?: throw Exception("Could not open image stream")
            val imageBytes = inputStream.readBytes()
            val base64Image = "data:image/jpeg;base64," + Base64.encodeToString(imageBytes, Base64.DEFAULT)

            // 2. Construct PostModel storing the Base64 string in imageUrl
            val post = PostModel(
                id = postId,
                userId = currentUser.uid,
                caption = caption,
                imageUrl = base64Image,
                timestamp = System.currentTimeMillis(),
                likesCount = 0,
                likedByUsers = emptyMap()
            )

            // 3. Save post document directly to Firestore ([SCRUM-16](https://giojones.atlassian.net/browse/SCRUM-16))
            firestore.collection("posts").document(postId).set(post).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}