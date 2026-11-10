package com.gio.sabor_android.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gio.sabor_android.data.model.PostModel
import com.gio.sabor_android.data.repository.FeedRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FeedViewModel : ViewModel() {
    private val repository = FeedRepository()
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    // Real-time stream of posts from Firestore (SCRUM-10)
    val posts: StateFlow<List<PostModel>> = repository.getFeedPosts()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Trigger like toggle in Firestore (SCRUM-11)
    fun toggleLike(postId: String, isCurrentlyLiked: Boolean) {
        if (currentUserId != null) {
            repository.toggleLike(
                postId = postId,
                userId = currentUserId,
                isCurrentlyLiked = isCurrentlyLiked,
                onSuccess = { /* Successfully updated in Firestore */ },
                onError = { /* Handle error if needed */ }
            )
        }
    }
}