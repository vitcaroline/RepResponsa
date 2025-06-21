package com.example.represponsa.data.repository

import com.example.represponsa.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun register(user: User, password: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(user.email, password)
                .await()

            val uid = authResult.user?.uid ?: throw Exception("UID não encontrado")

            val userToSave = user.copy(uid = uid)

            firestore.collection("users")
                .document(uid)
                .set(userToSave)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    suspend fun getCurrentUser(): User? {
        val uid = firebaseAuth.currentUser?.uid ?: return null

        return try {
            val snapshot = firestore.collection("users")
                .document(uid)
                .get()
                .await()

            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }
}