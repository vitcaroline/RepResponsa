package com.example.represponsa.data.repository

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ReceiptRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val authRepository: AuthRepository
) {

    suspend fun uploadReceipt(fileUri: Uri, context: Context): Result<Unit> {
        return try {
            val currentUser = authRepository.getCurrentUser()
                ?: throw Exception("Usuário não logado")

            val republicId = currentUser.republicId
            val userId = currentUser.uid
            val sdf = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.getDefault())
            val month = sdf.format(System.currentTimeMillis())
            val fileName = fileUri.lastPathSegment ?: "comprovante"

            val inputStream = context.contentResolver.openInputStream(fileUri)
                ?: throw Exception("Erro ao abrir arquivo")
            val bytes = inputStream.readBytes()
            val fileBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)

            val receiptData = mapOf(
                "userId" to userId,
                "republicId" to republicId,
                "month" to month,
                "uploadDate" to FieldValue.serverTimestamp(),
                "fileName" to fileName,
                "fileContentBase64" to fileBase64
            )

            firestore.collection("receipts").add(receiptData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun hasUserPaidForMonth(userId: String, republicId: String, month: String): Boolean {
        return try {
            val snapshot = firestore.collection("receipts")
                .whereEqualTo("userId", userId)
                .whereEqualTo("republicId", republicId)
                .whereEqualTo("month", month)
                .get()
                .await()

            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }
}