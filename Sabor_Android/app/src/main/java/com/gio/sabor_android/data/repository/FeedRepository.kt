package com.gio.sabor_android.data.repository

import com.gio.sabor_android.data.model.PostModel // Make sure this matches your PostModel package
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FeedRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Real-time listener for your posts collection
    fun getFeedPosts(): Flow<List<PostModel>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot: QuerySnapshot?, exception: FirebaseFirestoreException? ->
                if (exception != null) {
                    // Handle query error here if needed
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val posts = snapshot.documents.mapNotNull { doc: DocumentSnapshot ->
                        // Map Firestore document to your PostModel data class
                        doc.toObject(PostModel::class.java)?.copy(id = doc.id)
                    }
                    trySend(posts)
                }
            }

        // Clean up the snapshot listener when the flow stops collecting
        awaitClose { listener.remove() }
    }
}