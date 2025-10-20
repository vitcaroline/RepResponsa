package com.example.represponsa.data.repository

import android.content.Context
import android.util.Log
import com.example.represponsa.data.cacheConfig.UserPreferences
import com.example.represponsa.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Calendar

class AuthRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val context: Context
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

            UserPreferences.saveUser(context, userToSave)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val authResult = firebaseAuth
                .signInWithEmailAndPassword(email, password)
                .await()

            val uid = authResult.user?.uid ?: throw Exception("Usuário não encontrado")

            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java)
                ?: throw Exception("Erro ao recuperar os dados do usuário")

            UserPreferences.saveUser(context, user)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        firebaseAuth.signOut()
        UserPreferences.clear(context)
    }

    suspend fun getCurrentUser(forceRefresh: Boolean = false): User? {
        val uid = firebaseAuth.currentUser?.uid ?: return null

        if (!forceRefresh) {
            val cachedUser = UserPreferences.getUser(context)
            if (cachedUser != null) return cachedUser
        }

        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java)
            if (user != null) {
                UserPreferences.saveUser(context, user)
            }
            user
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUser(updatedUser: User) {
        val uid = firebaseAuth.currentUser?.uid ?: throw Exception("Usuário não autenticado")

        firestore.collection("users").document(uid)
            .set(updatedUser)
            .await()

        UserPreferences.saveUser(context, updatedUser)
    }

    suspend fun addMonthlyPointToCurrentUser() {
        val user = getCurrentUser(forceRefresh = true) ?: return

        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val points = if (user.lastPointsResetMonth != currentMonth) 0 else user.monthlyPoints

        val newPoints = points + 1
        val updatedUser = user.copy(
            monthlyPoints = newPoints,
            lastPointsResetMonth = currentMonth
        )

        val uid = firebaseAuth.currentUser?.uid ?: return

        firestore.collection("users").document(uid)
            .set(
                mapOf(
                    "monthlyPoints" to newPoints,
                    "lastPointsResetMonth" to currentMonth
                ),
                com.google.firebase.firestore.SetOptions.merge()
            )
            .await()

        UserPreferences.saveUser(context, updatedUser)

        Log.d("PointsDebug", "Firestore atualizado: $newPoints pontos")
    }
}